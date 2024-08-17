package com.juriba.tracker.expense.application.imp;

import com.juriba.tracker.auth.application.GetAuthenticatedUserUseCase;
import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.common.domain.EventPublisher;
import com.juriba.tracker.common.domain.exception.ConflictException;
import com.juriba.tracker.expense.application.CreateCategoryUseCase;
import com.juriba.tracker.expense.domain.Category;
import com.juriba.tracker.expense.infrastructure.CategoryRepository;
import com.juriba.tracker.expense.presentation.dto.CategoryRequest;
import com.juriba.tracker.expense.presentation.dto.CategoryResponse;
import com.juriba.tracker.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;


@Slf4j
@UseCase
public class CreateCategoryUseCaseImp implements CreateCategoryUseCase {
    private final CategoryRepository categoryRepository;
    private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;
    private final EventPublisher eventPublisher;

    public CreateCategoryUseCaseImp(CategoryRepository categoryRepository, GetAuthenticatedUserUseCase getAuthenticatedUserUseCase, EventPublisher eventPublisher) {
        this.categoryRepository = categoryRepository;
        this.getAuthenticatedUserUseCase = getAuthenticatedUserUseCase;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public CategoryResponse execute(CategoryRequest request) {
        User currentUser = getAuthenticatedUserUseCase.execute();

        String trimmedCategoryName = request.name().trim();
        if (categoryRepository.existsByNameAndOwner_Id(trimmedCategoryName, currentUser.getId())) {
            throw new ConflictException("Category with this name already exists for the current user: " + currentUser.getEmail());
        }
        Category category = new Category(trimmedCategoryName, currentUser, false);
        category = categoryRepository.save(category);
        category.getDomainEvents().forEach(eventPublisher::publish);
        category.clearDomainEvents();
        return new CategoryResponse(category.getId(), category.getName(), category.isDefault());
    }
}