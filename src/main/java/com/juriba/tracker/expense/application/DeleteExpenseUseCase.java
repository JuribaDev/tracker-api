package com.juriba.tracker.expense.application;

import com.juriba.tracker.common.presentation.dto.CommonSuccessResponse;

public interface DeleteExpenseUseCase {
    CommonSuccessResponse execute(String expenseId);
}
