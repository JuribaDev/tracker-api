package com.juriba.tracker.expense.application.imp;

import com.juriba.tracker.auth.application.GetAuthenticatedUserUseCase;
import com.juriba.tracker.common.domain.EventPublisher;
import com.juriba.tracker.common.domain.exception.ResourceNotFoundException;
import com.juriba.tracker.expense.domain.Category;
import com.juriba.tracker.expense.domain.Expense;
import com.juriba.tracker.expense.infrastructure.CategoryRepository;
import com.juriba.tracker.expense.infrastructure.ExpenseRepository;
import com.juriba.tracker.expense.presentation.dto.ExpenseResponse;
import com.juriba.tracker.expense.presentation.dto.UpdateExpenseRequest;
import com.juriba.tracker.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateExpenseUseCaseImpTest {

    @Mock
    private ExpenseRepository expenseRepository;
    @Mock
    private CategoryRepository categoryRepository;
    @Mock
    private GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;
    @Mock
    private EventPublisher eventPublisher;

    private UpdateExpenseUseCaseImp updateExpenseUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        updateExpenseUseCase = new UpdateExpenseUseCaseImp(expenseRepository, categoryRepository, getAuthenticatedUserUseCase, eventPublisher);
    }

    @Test
    void execute_shouldUpdateExpense_whenValidRequest() {
        // Arrange
        String expenseId = "expense123";
        User user = new User("John", "john@example.com", "password");
        user.setId("user123");
        Category category = new Category("Food", user, false);
        category.setId("category123");
        Expense existingExpense = new Expense("Lunch", BigDecimal.TEN, category, user);
        existingExpense.setId(expenseId);

        UpdateExpenseRequest request = new UpdateExpenseRequest("Dinner", BigDecimal.valueOf(20), "category123");

        when(getAuthenticatedUserUseCase.execute()).thenReturn(user);
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(existingExpense));
        when(categoryRepository.findById("category123")).thenReturn(Optional.of(category));
        when(expenseRepository.save(any(Expense.class))).thenReturn(existingExpense);

        // Act
        ExpenseResponse response = updateExpenseUseCase.execute(expenseId, request);

        // Assert
        assertNotNull(response);
        assertEquals("Dinner", response.getDescription());
        assertEquals(BigDecimal.valueOf(20), response.getAmount());
        assertEquals("category123", response.getCategory().getId());

        verify(expenseRepository).findById(expenseId);
        verify(categoryRepository).findById("category123");
        verify(expenseRepository).save(any(Expense.class));
        verify(eventPublisher, atLeastOnce()).publish(any());
    }

    @Test
    void execute_shouldThrowResourceNotFoundException_whenExpenseNotFound() {
        // Arrange
        String expenseId = "nonexistent";
        User user = new User("John", "john@example.com", "password");
        user.setId("user123");
        UpdateExpenseRequest request = new UpdateExpenseRequest("Dinner", BigDecimal.valueOf(20), "category123");

        when(getAuthenticatedUserUseCase.execute()).thenReturn(user);
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> updateExpenseUseCase.execute(expenseId, request));

        verify(expenseRepository).findById(expenseId);
        verify(categoryRepository, never()).findById(any());
        verify(expenseRepository, never()).save(any());
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    void execute_shouldThrowResourceNotFoundException_whenCategoryNotFound() {
        // Arrange
        String expenseId = "expense123";
        User user = new User("John", "john@example.com", "password");
        user.setId("user123");
        Category category = new Category("Food", user, false);
        category.setId("category123");
        Expense existingExpense = new Expense("Lunch", BigDecimal.TEN, category, user);
        existingExpense.setId(expenseId);

        UpdateExpenseRequest request = new UpdateExpenseRequest("Dinner", BigDecimal.valueOf(20), "nonexistent");

        when(getAuthenticatedUserUseCase.execute()).thenReturn(user);
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(existingExpense));
        when(categoryRepository.findById("nonexistent")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> updateExpenseUseCase.execute(expenseId, request));

        verify(expenseRepository).findById(expenseId);
        verify(categoryRepository).findById("nonexistent");
        verify(expenseRepository, never()).save(any());
        verify(eventPublisher, never()).publish(any());
    }
}