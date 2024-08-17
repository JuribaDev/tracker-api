package com.juriba.tracker.expense.application.imp;

import com.juriba.tracker.auth.application.GetAuthenticatedUserUseCase;
import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.common.domain.exception.ResourceNotFoundException;
import com.juriba.tracker.expense.application.GetCategoryUseCase;
import com.juriba.tracker.expense.domain.Category;
import com.juriba.tracker.expense.infrastructure.CategoryRepository;
import com.juriba.tracker.expense.presentation.dto.CategoryResponse;
import com.juriba.tracker.expense.presentation.mapper.CategoryMapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@UseCase
public class GetCategoryUseCaseImpl implements GetCategoryUseCase {
    private final CategoryRepository categoryRepository;
    private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;

    public GetCategoryUseCaseImpl(CategoryRepository categoryRepository, GetAuthenticatedUserUseCase getAuthenticatedUserUseCase) {
        this.categoryRepository = categoryRepository;
        this.getAuthenticatedUserUseCase = getAuthenticatedUserUseCase;
    }

    @Override
    @Transactional(readOnly = true)
    public CategoryResponse execute(String id) {
        String userId = getAuthenticatedUserUseCase.execute().getId();
        Category category = categoryRepository.findById(id).orElseThrow(()->  new ResourceNotFoundException("Category not found"));
        if (!isEquals(category, userId)) {
            log.error("User {} is not the owner of the category {}", userId, id);
            throw new ResourceNotFoundException("Category not found");
        }
        return CategoryMapper.toCategoryResponse(category);
    }

    private boolean isEquals(Category category, String userId) {
        return category.getOwner().getId().equals(userId);
    }
}
