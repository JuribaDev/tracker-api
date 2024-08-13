package com.juriba.tracker.user.domain.event;

import com.juriba.tracker.common.application.DomainEvent;
import com.juriba.tracker.user.domain.Role;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
public class RoleCreatedEvent implements DomainEvent {
    private final Role role;
    private final OffsetDateTime occurredOn;
    public RoleCreatedEvent(Role role) {
        this.role = role;
        this.occurredOn = OffsetDateTime.now();
    }

    @Override
    public OffsetDateTime occurredOn() {
        return occurredOn;
    }
}
