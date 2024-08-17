package com.juriba.tracker.expense.application;

import com.juriba.tracker.expense.presentation.dto.CategoryResponse;

public interface GetCategoryUseCase {
    CategoryResponse execute(String id);
}
