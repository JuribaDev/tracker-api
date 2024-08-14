package com.juriba.tracker.expense.application;

import com.juriba.tracker.expense.presentation.dto.ExpenseResponse;
import com.juriba.tracker.expense.presentation.dto.UpdateExpenseRequest;

public interface UpdateExpenseUseCase {
    ExpenseResponse execute(String id, UpdateExpenseRequest request);
}
