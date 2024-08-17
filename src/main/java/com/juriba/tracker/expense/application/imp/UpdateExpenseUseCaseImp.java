package com.juriba.tracker.expense.application.imp;

import com.juriba.tracker.auth.application.GetAuthenticatedUserUseCase;
import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.common.domain.EventPublisher;
import com.juriba.tracker.common.domain.exception.ResourceNotFoundException;
import com.juriba.tracker.expense.application.UpdateExpenseUseCase;
import com.juriba.tracker.expense.domain.Category;
import com.juriba.tracker.expense.domain.Expense;
import com.juriba.tracker.expense.infrastructure.CategoryRepository;
import com.juriba.tracker.expense.infrastructure.ExpenseRepository;
import com.juriba.tracker.expense.presentation.dto.ExpenseResponse;
import com.juriba.tracker.expense.presentation.dto.UpdateExpenseRequest;
import com.juriba.tracker.expense.presentation.mapper.ExpenseMapper;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@UseCase
public class UpdateExpenseUseCaseImp implements UpdateExpenseUseCase {
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;
    private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;
    private final EventPublisher eventPublisher;

    public UpdateExpenseUseCaseImp(ExpenseRepository expenseRepository, CategoryRepository categoryRepository, GetAuthenticatedUserUseCase getAuthenticatedUserUseCase, EventPublisher eventPublisher) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
        this.getAuthenticatedUserUseCase = getAuthenticatedUserUseCase;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public ExpenseResponse execute(String id, UpdateExpenseRequest request) {
        var userId = getAuthenticatedUserUseCase.execute().getId();

        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
        if (!isEquals(expense, userId)) {
            log.error("User {} is not the owner of the expense {}", userId, id);
            throw  new ResourceNotFoundException("Category not found");
        }

        Category category = categoryRepository.findById(request.categoryId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        expense.update(request.description(), request.amount(), category);
        expense = expenseRepository.save(expense);
        expense.getDomainEvents().forEach(eventPublisher::publish);
        expense.clearDomainEvents();
        return ExpenseMapper.toExpenseResponse(expense);
    }

    private  boolean isEquals(Expense expense, String userId) {
        return expense.getOwner().getId().equals(userId);
    }
}
