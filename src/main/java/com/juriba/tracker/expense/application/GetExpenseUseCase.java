package com.juriba.tracker.expense.application;

import com.juriba.tracker.expense.presentation.dto.ExpenseResponse;

public interface GetExpenseUseCase {
    ExpenseResponse execute(String id);
}
