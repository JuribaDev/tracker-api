package com.juriba.tracker.expense.application;

import com.juriba.tracker.expense.presentation.dto.ExpenseRequest;
import com.juriba.tracker.expense.presentation.dto.ExpenseResponse;

public interface CreateExpenseUseCase {
    ExpenseResponse execute(ExpenseRequest request);
}
