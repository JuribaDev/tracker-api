package com.juriba.tracker.user.domain.event;

import com.juriba.tracker.common.application.DomainEvent;
import com.juriba.tracker.user.domain.User;
import lombok.Getter;
import java.time.OffsetDateTime;

@Getter
public class UserCreatedEvent implements DomainEvent {
    private final User user;
    private final OffsetDateTime occurredOn;
    public UserCreatedEvent(User user) {
        this.user = user;
        this.occurredOn = OffsetDateTime.now();
    }


    @Override
    public OffsetDateTime occurredOn() {
        return occurredOn;
    }
}
