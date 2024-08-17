package com.juriba.tracker.expense.application.imp;

import com.juriba.tracker.auth.application.GetAuthenticatedUserUseCase;
import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.common.domain.EventPublisher;
import com.juriba.tracker.common.domain.exception.ResourceNotFoundException;
import com.juriba.tracker.common.presentation.dto.CommonSuccessResponse;
import com.juriba.tracker.expense.application.DeleteCategoryUseCase;
import com.juriba.tracker.expense.domain.Category;
import com.juriba.tracker.expense.infrastructure.CategoryRepository;
import com.juriba.tracker.expense.infrastructure.ExpenseRepository;
import com.juriba.tracker.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@UseCase
public class DeleteCategoryUseCaseImpl implements DeleteCategoryUseCase {
    private final CategoryRepository categoryRepository;
    private final ExpenseRepository expenseRepository;
    private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;
    private final EventPublisher eventPublisher;

    public DeleteCategoryUseCaseImpl(CategoryRepository categoryRepository, ExpenseRepository expenseRepository, GetAuthenticatedUserUseCase getAuthenticatedUserUseCase, EventPublisher eventPublisher) {
        this.categoryRepository = categoryRepository;
        this.expenseRepository = expenseRepository;
        this.getAuthenticatedUserUseCase = getAuthenticatedUserUseCase;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public CommonSuccessResponse execute(String categoryId) {
        User user = getAuthenticatedUserUseCase.execute();
        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));
        if (!isEquals(category, user.getId())) {
            log.error("User {} is not the owner of the category {}", user.getEmail(), categoryId);
            throw new ResourceNotFoundException("Category not found");
        }

        moveUserExpensesToUnlinkedCategory(categoryId, user);
        category.deleteCategory();
        category.getDomainEvents().forEach(eventPublisher::publish);
        category.clearDomainEvents();
        categoryRepository.deleteById(category.getId());
        return new CommonSuccessResponse("Category deleted successfully");
    }
    private void moveUserExpensesToUnlinkedCategory(String categoryId, User user) {
        var relatedExpenses = expenseRepository.findAllByCategory_Id(categoryId);
        var unlinkCategory = categoryRepository.findByNameAndOwner_Id("Unlinked", user.getId())
                .orElseGet(()->  categoryRepository.save(new Category("Unlinked", user,true)));

        relatedExpenses.forEach(expense -> {
            expense.setCategory(unlinkCategory);
            expenseRepository.save(expense);
        });
    }

    private boolean isEquals(Category category, String userId) {
        return category.getOwner().getId().equals(userId);
    }
}
