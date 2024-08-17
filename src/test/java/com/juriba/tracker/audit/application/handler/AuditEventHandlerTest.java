package com.juriba.tracker.audit.application.handler;

import com.juriba.tracker.audit.application.LogEventUseCase;
import com.juriba.tracker.user.domain.Role;
import com.juriba.tracker.user.domain.User;
import com.juriba.tracker.user.domain.event.*;
import com.juriba.tracker.auth.domain.AuthenticationAttempt;
import com.juriba.tracker.auth.domain.event.AuthenticationAttemptedEvent;
import com.juriba.tracker.expense.domain.Category;
import com.juriba.tracker.expense.domain.Expense;
import com.juriba.tracker.expense.domain.event.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.verify;

class AuditEventHandlerTest {

    @Mock
    private LogEventUseCase logEventUseCase;

    private AuditEventHandler auditEventHandler;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        auditEventHandler = new AuditEventHandler(logEventUseCase);
    }

    @Test
    void handleUserCreatedEvent() {
        User user = new User("John Doe", "john@example.com", "password");
        UserCreatedEvent event = new UserCreatedEvent(user);
        auditEventHandler.handleUserCreatedEvent(event);
        verify(logEventUseCase).execute(eq(event), eq(user.getId()), eq("User"), eq("Created"),
                anyString(), eq(user.getCreatedBy()));
    }

    @Test
    void handleUserPasswordChangedEvent() {
        User user = new User("John Doe", "john@example.com", "password");
        UserPasswordChangedEvent event = new UserPasswordChangedEvent(user);
        auditEventHandler.handleUserPasswordChangedEvent(event);
        verify(logEventUseCase).execute(eq(event), eq(user.getId()), eq("User"), eq("PasswordChanged"),
                anyString(), eq(user.getModifiedBy()));
    }

    @Test
    void handleUserRoleAddedEvent() {
        User user = new User("John Doe", "john@example.com", "password");
        Role role = new Role("ADMIN");
        UserRoleAddedEvent event = new UserRoleAddedEvent(user, role);
        auditEventHandler.handleUserRoleAddedEvent(event);
        verify(logEventUseCase).execute(eq(event), eq(user.getId()), eq("User"), eq("RoleAdded"),
                anyString(), eq(user.getModifiedBy()));
    }

    @Test
    void handleAuthenticationAttemptedEvent() {
        AuthenticationAttempt attempt = new AuthenticationAttempt("john@example.com", true);
        AuthenticationAttemptedEvent event = new AuthenticationAttemptedEvent(attempt);
        auditEventHandler.handleAuthenticationAttemptedEvent(event);
        verify(logEventUseCase).execute(eq(event), eq(attempt.getId()), eq("Authentication"), eq("Attempted"),
                anyString(), eq("System"));
    }

    @Test
    void handleExpenseCreatedEvent() {
        User user = new User("John Doe", "john@example.com", "password");
        Category category = new Category("Food", user, false);
        Expense expense = new Expense("Lunch", BigDecimal.TEN, category, user);
        ExpenseCreatedEvent event = new ExpenseCreatedEvent(expense);
        auditEventHandler.handleExpenseCreatedEvent(event);
        verify(logEventUseCase).execute(eq(event), eq(expense.getId()), eq("Expense"), eq("Created"),
                anyString(), eq(expense.getCreatedBy()));
    }

    @Test
    void handleCategoryCreatedEvent() {
        User user = new User("John Doe", "john@example.com", "password");
        Category category = new Category("Food", user, false);
        CategoryCreatedEvent event = new CategoryCreatedEvent(category);
        auditEventHandler.handleCategoryCreatedEvent(event);
        verify(logEventUseCase).execute(eq(event), eq(category.getId()), eq("Category"), eq("Created"),
                anyString(), eq(category.getCreatedBy()));
    }
}