package com.juriba.tracker.audit.application.handler;


import com.juriba.tracker.audit.application.LogEventUseCase;
import com.juriba.tracker.user.domain.event.*;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
class AuditEventHandler {

    private final LogEventUseCase logEventUseCase;

    public AuditEventHandler(LogEventUseCase logEventUseCase) {
        this.logEventUseCase = logEventUseCase;
    }

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
    public void handleRoleCreatedEvent(RoleCreatedEvent event) {
        logEventUseCase.execute(event, event.getRole().getId(), "Role", event.getClass().getSimpleName(),
                "New Role Created: " + event.getRole().getName(), event.getRole().getCreatedBy());
    }
}