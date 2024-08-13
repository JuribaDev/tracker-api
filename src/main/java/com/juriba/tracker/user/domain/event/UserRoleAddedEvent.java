package com.juriba.tracker.user.domain.event;

import com.juriba.tracker.common.application.DomainEvent;
import com.juriba.tracker.user.domain.Role;
import com.juriba.tracker.user.domain.User;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
public class UserRoleAddedEvent implements DomainEvent {
    private final User user;
    private final Role role;
    private final OffsetDateTime occurredOn;

    public UserRoleAddedEvent(User user, Role role) {
        this.user = user;
        this.role = role;
        this.occurredOn = OffsetDateTime.now();
    }

    @Override
    public OffsetDateTime occurredOn() {
        return occurredOn;
    }
}
