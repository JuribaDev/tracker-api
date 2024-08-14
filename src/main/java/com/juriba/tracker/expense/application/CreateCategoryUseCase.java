package com.juriba.tracker.expense.application;

import com.juriba.tracker.expense.presentation.dto.CategoryRequest;
import com.juriba.tracker.expense.presentation.dto.CategoryResponse;

public interface CreateCategoryUseCase {
     CategoryResponse execute(CategoryRequest request);
}
