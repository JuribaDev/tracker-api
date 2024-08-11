package com.juriba.tracker.user.domain.event;

import com.juriba.tracker.common.application.DomainEvent;
import com.juriba.tracker.user.domain.Role;
import com.juriba.tracker.user.domain.User;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
public class UserRoleRemovedEvent implements DomainEvent {
    private final User user;
    private final OffsetDateTime occurredOn;
    private final Role role;
    public UserRoleRemovedEvent(User user, Role role) {
        this.user = user;
        this.role = role;
        this.occurredOn = OffsetDateTime.now();
    }


    @Override
    public OffsetDateTime occurredOn() {
        return occurredOn;
    }
}
