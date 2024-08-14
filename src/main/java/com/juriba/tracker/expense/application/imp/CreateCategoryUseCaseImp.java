package com.juriba.tracker.expense.application.imp;

import com.juriba.tracker.auth.application.GetAuthenticatedUserUseCase;
import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.common.domain.exception.ResourceNotFoundException;
import com.juriba.tracker.common.domain.exception.ConflictException;
import com.juriba.tracker.expense.application.CreateCategoryUseCase;
import com.juriba.tracker.expense.domain.Category;
import com.juriba.tracker.expense.infrastructure.CategoryRepository;
import com.juriba.tracker.expense.presentation.dto.CategoryRequest;
import com.juriba.tracker.expense.presentation.dto.CategoryResponse;
import com.juriba.tracker.user.domain.User;
import com.juriba.tracker.user.infrastructure.UserRepository;
import org.springframework.transaction.annotation.Transactional;


@UseCase
public class CreateCategoryUseCaseImp implements CreateCategoryUseCase {
    private final CategoryRepository categoryRepository;
    private final UserRepository userRepository;
    private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;

    public CreateCategoryUseCaseImp(CategoryRepository categoryRepository, UserRepository userRepository, GetAuthenticatedUserUseCase getAuthenticatedUserUseCase) {
        this.categoryRepository = categoryRepository;
        this.userRepository = userRepository;
        this.getAuthenticatedUserUseCase = getAuthenticatedUserUseCase;
    }

    @Override
    @Transactional
    public CategoryResponse execute(CategoryRequest request) {
        String currentUserEmail = getAuthenticatedUserUseCase.execute().getEmail();
        User currentUser = userRepository.findByEmail(currentUserEmail)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));

        if (categoryRepository.existsByNameAndOwner(request.name(), currentUser)) {
            throw new ConflictException("Category with this name already exists for the current user");
        }

        Category category = new Category(request.name(), currentUser, false);
        Category savedCategory = categoryRepository.save(category);

        return new CategoryResponse(savedCategory.getId(), savedCategory.getName(), savedCategory.isDefault());
    }
}