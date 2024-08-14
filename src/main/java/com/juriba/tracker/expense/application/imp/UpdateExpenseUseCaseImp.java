package com.juriba.tracker.expense.application.imp;

import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.common.domain.exception.ResourceNotFoundException;
import com.juriba.tracker.expense.application.UpdateExpenseUseCase;
import com.juriba.tracker.expense.domain.Category;
import com.juriba.tracker.expense.domain.Expense;
import com.juriba.tracker.expense.infrastructure.CategoryRepository;
import com.juriba.tracker.expense.infrastructure.ExpenseRepository;
import com.juriba.tracker.expense.presentation.dto.ExpenseRequest;
import com.juriba.tracker.expense.presentation.dto.ExpenseResponse;
import com.juriba.tracker.expense.presentation.dto.UpdateExpenseRequest;
import com.juriba.tracker.expense.presentation.mapper.ExpenseMapper;
import jakarta.transaction.Transactional;

@UseCase
public class UpdateExpenseUseCaseImp implements UpdateExpenseUseCase {
    private final ExpenseRepository expenseRepository;
    private final CategoryRepository categoryRepository;

    public UpdateExpenseUseCaseImp(ExpenseRepository expenseRepository, CategoryRepository categoryRepository) {
        this.expenseRepository = expenseRepository;
        this.categoryRepository = categoryRepository;
    }

    @Override
    @Transactional
    public ExpenseResponse execute(String id, UpdateExpenseRequest request) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        Category category = categoryRepository.findById(expense.getCategory().getId())
                .orElseThrow(() -> new ResourceNotFoundException("Category not found"));

        expense.update(request.description(), request.amount(), category);
        Expense updatedExpense = expenseRepository.save(expense);

        return ExpenseMapper.toExpenseResponse(updatedExpense);
    }
}
