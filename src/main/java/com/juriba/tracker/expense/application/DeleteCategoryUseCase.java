package com.juriba.tracker.expense.application;

import com.juriba.tracker.common.presentation.dto.CommonSuccessResponse;

public interface DeleteCategoryUseCase {
    CommonSuccessResponse execute(String categoryId);
}
