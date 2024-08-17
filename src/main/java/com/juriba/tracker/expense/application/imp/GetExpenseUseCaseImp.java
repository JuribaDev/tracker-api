package com.juriba.tracker.expense.application.imp;

import com.juriba.tracker.auth.application.GetAuthenticatedUserUseCase;
import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.common.domain.exception.ResourceNotFoundException;
import com.juriba.tracker.expense.application.GetExpenseUseCase;
import com.juriba.tracker.expense.domain.Expense;
import com.juriba.tracker.expense.infrastructure.ExpenseRepository;
import com.juriba.tracker.expense.presentation.dto.ExpenseResponse;
import com.juriba.tracker.expense.presentation.mapper.ExpenseMapper;
import com.juriba.tracker.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@UseCase
public class GetExpenseUseCaseImp implements GetExpenseUseCase {
    private final ExpenseRepository expenseRepository;
    private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;

    public GetExpenseUseCaseImp(ExpenseRepository expenseRepository, GetAuthenticatedUserUseCase getAuthenticatedUserUseCase) {
        this.expenseRepository = expenseRepository;
        this.getAuthenticatedUserUseCase = getAuthenticatedUserUseCase;
    }

    @Override
    @Transactional(readOnly = true)
    public ExpenseResponse execute(String id) {
        User user = getAuthenticatedUserUseCase.execute();
        Expense expense = expenseRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
        if(!isEquals(expense, user.getId())) {
            log.error("User {} is not the owner of the expense {}", user.getEmail(), id);
            throw new ResourceNotFoundException("Expense not found");
        }
        return ExpenseMapper.toExpenseResponse(expense);
    }

    private  boolean isEquals(Expense expense, String userId) {
        return expense.getOwner().getId().equals(userId);
    }
}
