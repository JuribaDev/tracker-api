package com.juriba.tracker.expense.application.imp;

import com.juriba.tracker.auth.application.GetAuthenticatedUserUseCase;
import com.juriba.tracker.common.domain.EventPublisher;
import com.juriba.tracker.common.domain.exception.ResourceNotFoundException;
import com.juriba.tracker.common.presentation.dto.CommonSuccessResponse;
import com.juriba.tracker.expense.domain.Expense;
import com.juriba.tracker.expense.infrastructure.ExpenseRepository;
import com.juriba.tracker.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class DeleteExpenseUseCaseImplTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;

    @Mock
    private EventPublisher eventPublisher;

    private DeleteExpenseUseCaseImpl deleteExpenseUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deleteExpenseUseCase = new DeleteExpenseUseCaseImpl(expenseRepository, getAuthenticatedUserUseCase, eventPublisher);
    }

    @Test
    void execute_shouldDeleteExpense_whenValidExpenseIdAndOwner() {
        // Arrange
        String expenseId = "expense123";
        User user = new User("John", "john@example.com", "password");
        user.setId("user123");
        Expense expense = mock(Expense.class);
        when(expense.getOwner()).thenReturn(user);

        when(getAuthenticatedUserUseCase.execute()).thenReturn(user);
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(expense));

        // Act
        CommonSuccessResponse response = deleteExpenseUseCase.execute(expenseId);

        // Assert
        assertNotNull(response);
        assertEquals("Expense deleted successfully", response.message());

        verify(expenseRepository).findById(expenseId);
        verify(expense).deleteExpense();
    }

    @Test
    void execute_shouldThrowResourceNotFoundException_whenExpenseNotFound() {
        // Arrange
        String expenseId = "nonexistent123";
        User user = new User("John", "john@example.com", "password");
        user.setId("user123");

        when(getAuthenticatedUserUseCase.execute()).thenReturn(user);
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> deleteExpenseUseCase.execute(expenseId));

        verify(expenseRepository).findById(expenseId);
        verify(expenseRepository, never()).deleteById(any());
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    void execute_shouldThrowResourceNotFoundException_whenUserIsNotOwner() {
        // Arrange
        String expenseId = "expense123";
        User user = new User("John", "john@example.com", "password");
        user.setId("user123");
        User otherUser = new User("Jane", "jane@example.com", "password");
        otherUser.setId("user456");
        Expense expense = mock(Expense.class);
        when(expense.getOwner()).thenReturn(otherUser);

        when(getAuthenticatedUserUseCase.execute()).thenReturn(user);
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(expense));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> deleteExpenseUseCase.execute(expenseId));

        verify(expenseRepository).findById(expenseId);
        verify(expenseRepository, never()).deleteById(any());
        verify(eventPublisher, never()).publish(any());
    }
}