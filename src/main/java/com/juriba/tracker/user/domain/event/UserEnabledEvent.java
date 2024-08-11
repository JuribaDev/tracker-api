package com.juriba.tracker.user.domain.event;

import com.juriba.tracker.common.application.DomainEvent;
import com.juriba.tracker.user.domain.User;
import lombok.Data;

import java.time.OffsetDateTime;

@Data
public class UserEnabledEvent implements DomainEvent {
    private final User user;
    private final OffsetDateTime occurredOn;
    public UserEnabledEvent(User user) {
        this.user = user;
        this.occurredOn = OffsetDateTime.now();
    }
    @Override
    public OffsetDateTime occurredOn() {
        return occurredOn;
    }
}
