package com.juriba.tracker.expense.application.imp;

import com.juriba.tracker.auth.application.GetAuthenticatedUserUseCase;
import com.juriba.tracker.expense.domain.Category;
import com.juriba.tracker.expense.domain.Expense;
import com.juriba.tracker.expense.infrastructure.ExpenseRepository;
import com.juriba.tracker.expense.presentation.dto.ExpenseResponse;
import com.juriba.tracker.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GetListExpensesUseCaseImpTest {

    @Mock
    private ExpenseRepository expenseRepository;

    @Mock
    private GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;

    private GetListExpensesUseCaseImp getListExpensesUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        getListExpensesUseCase = new GetListExpensesUseCaseImp(expenseRepository, getAuthenticatedUserUseCase);
    }

    @Test
    void execute_shouldReturnPagedExpenses_whenNoDateFiltersProvided() {
        // Arrange
        User user = new User("John", "john@example.com", "password");
        user.setId("user123");
        Category category = new Category("Food", user,true);
        category.setId("category123");
        when(getAuthenticatedUserUseCase.execute()).thenReturn(user);
        Pageable expensePage = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        when(expenseRepository.findAllByOwner_Id("user123",expensePage)).thenReturn(new PageImpl<>(Arrays.asList(
                new Expense("Expense 1", new BigDecimal(100), category, user),
                new Expense("Expense 2",new BigDecimal(200), category, user)
        )));

        // Act
        Page<ExpenseResponse> result = getListExpensesUseCase.execute(null, null, 0, 10, "createdAt", "DESC");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(expenseRepository).findAllByOwner_Id(eq("user123"), any(PageRequest.class));
        verify(expenseRepository, never()).findByOwner_IdAndCreatedAtBetween(any(), any(), any(), any());
    }

    @Test
    void execute_shouldReturnFilteredExpenses_whenDateFiltersProvided() {
        // Arrange
        User user = new User("John", "john@example.com", "password");
        user.setId("user123");
        Category category = new Category("Food", user,true);
        category.setId("category123");
        when(getAuthenticatedUserUseCase.execute()).thenReturn(user);
        Pageable expensePage = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "createdAt"));
        Instant startDate = Instant.parse("2023-01-01T00:00:00Z");
        Instant endDate = Instant.parse("2023-12-31T23:59:59Z");
        when(expenseRepository.findByOwner_IdAndCreatedAtBetween("user123",startDate,endDate,expensePage)).thenReturn(new PageImpl<>(Arrays.asList(
                new Expense("Expense 1", new BigDecimal(100), category, user),
                new Expense("Expense 2",new BigDecimal(200), category, user)
        )));


        // Act
        Page<ExpenseResponse> result = getListExpensesUseCase.execute(startDate, endDate, 0, 10, "createdAt", "DESC");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getContent().size());
        verify(expenseRepository).findByOwner_IdAndCreatedAtBetween(
                eq("user123"), eq(startDate), eq(endDate), any(PageRequest.class)
        );
        verify(expenseRepository, never()).findAllByOwner_Id(any(), any());
    }

}