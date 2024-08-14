package com.juriba.tracker.expense.application;

import com.juriba.tracker.expense.presentation.dto.ExpenseResponse;
import org.springframework.data.domain.Page;


import java.time.Instant;

public interface GetListExpensesUseCase {
    Page<ExpenseResponse> execute(Instant startDate, Instant endDate, int page, int size, String sortBy, String sortDirection);
}
