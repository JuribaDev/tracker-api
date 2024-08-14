package com.juriba.tracker.expense.application.imp;


import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.expense.application.GetListExpensesUseCase;
import com.juriba.tracker.expense.domain.Expense;
import com.juriba.tracker.expense.infrastructure.ExpenseRepository;
import com.juriba.tracker.expense.presentation.dto.ExpenseResponse;
import com.juriba.tracker.expense.presentation.mapper.ExpenseMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@UseCase
public class GetListExpensesUseCaseImp implements GetListExpensesUseCase {
    private final ExpenseRepository expenseRepository;

    public GetListExpensesUseCaseImp(ExpenseRepository expenseRepository) {
        this.expenseRepository = expenseRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<ExpenseResponse> execute(Instant startDate, Instant endDate, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Expense> expenses;
        if (startDate != null && endDate != null) {
            expenses = expenseRepository.findByCreatedAtBetween(startDate, endDate, pageable);
        } else {
            expenses = expenseRepository.findAll(pageable);
        }
        return expenses.map(ExpenseMapper::toExpenseResponse);
    }
}
