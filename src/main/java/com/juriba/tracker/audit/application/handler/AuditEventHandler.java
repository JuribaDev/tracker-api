package com.juriba.tracker.audit.application.handler;

import com.juriba.tracker.audit.application.LogEventUseCase;
import com.juriba.tracker.user.domain.event.*;
import com.juriba.tracker.auth.domain.event.*;
import com.juriba.tracker.expense.domain.event.*;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
class AuditEventHandler {

    private final LogEventUseCase logEventUseCase;

    public AuditEventHandler(LogEventUseCase logEventUseCase) {
        this.logEventUseCase = logEventUseCase;
    }

    // User events
    @Async("threadPoolTaskExecutor")
    @EventListener
    public void handleUserCreatedEvent(UserCreatedEvent event) {
        logEventUseCase.execute(event, event.getUser().getId(), "User", "Created",
                "User created with email: " + event.getUser().getEmail(), event.getUser().getCreatedBy());
    }

    @Async("threadPoolTaskExecutor")
    @EventListener
    public void handleUserPasswordChangedEvent(UserPasswordChangedEvent event) {
        logEventUseCase.execute(event, event.getUser().getId(), "User", "PasswordChanged",
                "Password changed for user: " + event.getUser().getEmail(), event.getUser().getModifiedBy());
    }

    @Async("threadPoolTaskExecutor")
    @EventListener
    public void handleUserRoleAddedEvent(UserRoleAddedEvent event) {
        logEventUseCase.execute(event, event.getUser().getId(), "User", "RoleAdded",
                "Role " + event.getRole().getName() + " added to user: " + event.getUser().getEmail(),
                event.getUser().getModifiedBy());
    }

    @Async("threadPoolTaskExecutor")
    @EventListener
    public void handleUserRoleRemovedEvent(UserRoleRemovedEvent event) {
        logEventUseCase.execute(event, event.getUser().getId(), "User", "RoleRemoved",
                "Role " + event.getRole().getName() + " removed from user: " + event.getUser().getEmail(),
                event.getUser().getModifiedBy());
    }

    @Async("threadPoolTaskExecutor")
    @EventListener
    public void handleUserDisabledEvent(UserDisabledEvent event) {
        logEventUseCase.execute(event, event.getUser().getId(), "User", "Disabled",
                "User account disabled: " + event.getUser().getEmail(), event.getUser().getModifiedBy());
    }

    @Async("threadPoolTaskExecutor")
    @EventListener
    public void handleUserEnabledEvent(UserEnabledEvent event) {
        logEventUseCase.execute(event, event.getUser().getId(), "User", "Enabled",
                "User account enabled: " + event.getUser().getEmail(), event.getUser().getModifiedBy());
    }

    @Async("threadPoolTaskExecutor")
    @EventListener
    public void handleUserAccountLockedEvent(UserAccountLockedEvent event) {
        logEventUseCase.execute(event, event.getUser().getId(), "User", "AccountLocked",
                "User account locked: " + event.getUser().getEmail(), event.getUser().getModifiedBy());
    }

    @Async("threadPoolTaskExecutor")
    @EventListener
    public void handleUserAccountUnlockedEvent(UserAccountUnlockedEvent event) {
        logEventUseCase.execute(event, event.getUser().getId(), "User", "AccountUnlocked",
                "User account unlocked: " + event.getUser().getEmail(), event.getUser().getModifiedBy());
    }

    @Async("threadPoolTaskExecutor")
    @EventListener
    public void handleUserCategoryAddedEvent(UserCategoryAddedEvent event) {
        logEventUseCase.execute(event, event.getUser().getId(), "User", "CategoryAdded",
                "Category " + event.getCategory().getName() + " added to user: " + event.getUser().getEmail(),
                event.getUser().getModifiedBy());
    }

    @Async("threadPoolTaskExecutor")
    @EventListener
    public void handleUserCategoryRemovedEvent(UserCategoryRemovedEvent event) {
        logEventUseCase.execute(event, event.getUser().getId(), "User", "CategoryRemoved",
                "Category " + event.getCategory().getName() + " removed from user: " + event.getUser().getEmail(),
                event.getUser().getModifiedBy());
    }

    // Role events
    @Async("threadPoolTaskExecutor")
    @EventListener
    public void handleRoleCreatedEvent(RoleCreatedEvent event) {
        logEventUseCase.execute(event, event.getRole().getId(), "Role", "Created",
                "New Role Created: " + event.getRole().getName(), event.getRole().getCreatedBy());
    }

    @Async("threadPoolTaskExecutor")
    @EventListener
    public void handleRoleNameChangedEvent(RoleNameChangedEvent event) {
        logEventUseCase.execute(event, event.getRole().getId(), "Role", "NameChanged",
                "Role name changed to: " + event.getNewRoleName(), event.getRole().getModifiedBy());
    }

    // Authentication events
    @Async("threadPoolTaskExecutor")
    @EventListener
    public void handleAuthenticationAttemptedEvent(AuthenticationAttemptedEvent event) {
        logEventUseCase.execute(event, event.getAttempt().getId(), "Authentication", "Attempted",
                "Authentication attempt for user: " + event.getAttempt().getEmail() +
                        ", Success: " + event.getAttempt().isSuccessful(),
                "System");
    }

    // Expense events
    @Async("threadPoolTaskExecutor")
    @EventListener
    public void handleExpenseCreatedEvent(ExpenseCreatedEvent event) {
        logEventUseCase.execute(event, event.getExpense().getId(), "Expense", "Created",
                "Expense created with amount: " + event.getExpense().getAmount(),
                event.getExpense().getCreatedBy());
    }

    @Async("threadPoolTaskExecutor")
    @EventListener
    public void handleExpenseUpdatedEvent(ExpenseUpdatedEvent event) {
        logEventUseCase.execute(event, event.getExpense().getId(), "Expense", "Updated",
                "Expense updated with new amount: " + event.getExpense().getAmount(),
                event.getExpense().getModifiedBy());
    }

    // Category events
    @Async("threadPoolTaskExecutor")
    @EventListener
    public void handleCategoryCreatedEvent(CategoryCreatedEvent event) {
        logEventUseCase.execute(event, event.getCategory().getId(), "Category", "Created",
                "Category created with name: " + event.getCategory().getName(),
                event.getCategory().getCreatedBy());
    }

    @Async("threadPoolTaskExecutor")
    @EventListener
    public void handleCategoryUpdatedEvent(CategoryUpdatedEvent event) {
        logEventUseCase.execute(event, event.getCategory().getId(), "Category", "Updated",
                "Category updated with name: " + event.getCategory().getName(),
                event.getCategory().getModifiedBy());
    }

    @Async("threadPoolTaskExecutor")
    @EventListener
    public void handleCategoryDeletedEvent(CategoryDeletedEvent event) {
        logEventUseCase.execute(event, event.getCategory().getId(), "Category", "Deleted",
                "Category deleted with name: " + event.getCategory().getName(),
                event.getCategory().getModifiedBy());
    }
}