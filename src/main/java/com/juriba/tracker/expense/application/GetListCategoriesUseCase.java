package com.juriba.tracker.expense.application;

import com.juriba.tracker.expense.presentation.dto.CategoryResponse;
import org.springframework.data.domain.Page;

public interface GetListCategoriesUseCase {
    Page<CategoryResponse> execute(int page, int size, String sortBy, String sortDirection);
}
