package com.juriba.tracker.user.domain.event;

import com.juriba.tracker.common.application.DomainEvent;
import com.juriba.tracker.user.domain.Role;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
public class RoleNameChangedEvent implements DomainEvent {
    private final Role role;
    private final String newRoleName;
    private final OffsetDateTime occurredOn;

    public RoleNameChangedEvent(Role role, String newName) {
        this.role = role;
        this.newRoleName = newName;
        this.occurredOn = OffsetDateTime.now();
    }

    @Override
    public OffsetDateTime occurredOn() {
        return occurredOn;
    }
}
