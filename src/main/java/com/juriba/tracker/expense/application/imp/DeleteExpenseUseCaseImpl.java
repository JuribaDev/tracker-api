package com.juriba.tracker.expense.application.imp;

import com.juriba.tracker.auth.application.GetAuthenticatedUserUseCase;
import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.common.domain.EventPublisher;
import com.juriba.tracker.common.domain.exception.ResourceNotFoundException;
import com.juriba.tracker.common.presentation.dto.CommonSuccessResponse;
import com.juriba.tracker.expense.application.DeleteExpenseUseCase;
import com.juriba.tracker.expense.domain.Expense;
import com.juriba.tracker.expense.infrastructure.ExpenseRepository;
import com.juriba.tracker.user.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.annotation.Transactional;

@UseCase
@Slf4j
public class DeleteExpenseUseCaseImpl implements DeleteExpenseUseCase {
    private final ExpenseRepository expenseRepository;
    private final GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;
    private final EventPublisher eventPublisher;

    public DeleteExpenseUseCaseImpl(ExpenseRepository expenseRepository, GetAuthenticatedUserUseCase getAuthenticatedUserUseCase, EventPublisher eventPublisher) {
        this.expenseRepository = expenseRepository;
        this.getAuthenticatedUserUseCase = getAuthenticatedUserUseCase;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public CommonSuccessResponse execute(String expenseId) {
        User user = getAuthenticatedUserUseCase.execute();
        var expense = expenseRepository.findById(expenseId)
                .orElseThrow(() -> new ResourceNotFoundException("Expense not found"));
        if (!isEquals(expense, user)) {
            log.error("User {} is not the owner of the expense {}", user.getId(), expenseId);
            throw new ResourceNotFoundException("Expense not found");
        }
        expense.deleteExpense();
        expense.getDomainEvents().forEach(eventPublisher::publish);
        expense.clearDomainEvents();
        expenseRepository.deleteById(expense.getId());
        return new CommonSuccessResponse("Expense deleted successfully");
    }

    private  boolean isEquals(Expense expense, User user) {
        return expense.getOwner().getId().equals(user.getId());
    }
}
