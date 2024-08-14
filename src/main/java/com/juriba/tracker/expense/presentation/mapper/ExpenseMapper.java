package com.juriba.tracker.expense.presentation.mapper;

import com.juriba.tracker.expense.domain.Expense;
import com.juriba.tracker.expense.presentation.ExpenseController;
import com.juriba.tracker.expense.presentation.dto.ExpenseResponse;
import com.juriba.tracker.expense.presentation.dto.CategoryResponse;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public class ExpenseMapper {
    public static ExpenseResponse toExpenseResponse(Expense expense) {
        ExpenseResponse response = new ExpenseResponse(
                expense.getId(),
                expense.getDescription(),
                expense.getAmount(),
                expense.getCreatedAt(),
                new CategoryResponse(
                        expense.getCategory().getId(),
                        expense.getCategory().getName(),
                        expense.getCategory().isDefault()
                )
        );

        response.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(ExpenseController.class)
                                .getExpense(expense.getId()))
                .withSelfRel());

        return response;
    }
}
