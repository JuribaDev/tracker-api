package com.juriba.tracker.expense.application.imp;

import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.common.domain.exception.ResourceNotFoundException;
import com.juriba.tracker.expense.application.GetExpenseUseCase;
import com.juriba.tracker.expense.domain.Expense;
import com.juriba.tracker.expense.infrastructure.ExpenseRepository;
import com.juriba.tracker.expense.presentation.dto.ExpenseResponse;
import com.juriba.tracker.expense.presentation.mapper.ExpenseMapper;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class GetExpenseUseCaseImp implements GetExpenseUseCase {
    private final ExpenseRepository expenseRepository;

    public GetExpenseUseCaseImp(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseResponse execute(String id) {
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));

        return ExpenseMapper.toExpenseResponse(expense);
    }
}
