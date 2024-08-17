package com.juriba.tracker.expense.application.imp;

import com.juriba.tracker.auth.application.GetAuthenticatedUserUseCase;
import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.common.domain.EventPublisher;
import com.juriba.tracker.common.domain.exception.ResourceNotFoundException;
import com.juriba.tracker.expense.application.UpdateCategoryUseCase;
import com.juriba.tracker.expense.domain.Category;
import com.juriba.tracker.expense.infrastructure.CategoryRepository;
import com.juriba.tracker.expense.presentation.dto.CategoryRequest;
import com.juriba.tracker.expense.presentation.dto.CategoryResponse;
import com.juriba.tracker.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@UseCase
public class UpdateCategoryUseCaseImpl implements UpdateCategoryUseCase {
    private final CategoryRepository categoryRepository;
    private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;
    private final EventPublisher eventPublisher;

    public UpdateCategoryUseCaseImpl(CategoryRepository categoryRepository, GetAuthenticatedUserUseCase getAuthenticatedUserUseCase, EventPublisher eventPublisher) {
        this.categoryRepository = categoryRepository;
        this.getAuthenticatedUserUseCase = getAuthenticatedUserUseCase;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public CategoryResponse execute(String categoryId, CategoryRequest categoryRequest) {
        User user = getAuthenticatedUserUseCase.execute();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        if (!isEquals(category, user)) {
            log.error("User {} is not the owner of the category {}", user.getEmail(), categoryId);
            throw new ResourceNotFoundException("Category not found");
        }
        category.updateCategory(categoryRequest.name());
        category.getDomainEvents().forEach(eventPublisher::publish);
        category.clearDomainEvents();
        categoryRepository.save(category);
        return new CategoryResponse(category.getId(), category.getName(), category.isDefault());
    }

    private boolean isEquals(Category category, User user) {
        return category.getOwner().getId().equals(user.getId());
    }
}
