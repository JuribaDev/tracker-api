package com.juriba.tracker.auth.domain.event;

import com.juriba.tracker.auth.domain.AuthenticationAttempt;
import com.juriba.tracker.common.application.DomainEvent;
import lombok.Getter;
import java.time.OffsetDateTime;

@Getter
public class AuthenticationAttemptedEvent implements DomainEvent {

    private final AuthenticationAttempt attempt;
    private final OffsetDateTime occurredOn;
    public AuthenticationAttemptedEvent(AuthenticationAttempt attempt) {
        this.attempt = attempt;
        this.occurredOn = OffsetDateTime.now();
    }
    @Override
    public OffsetDateTime occurredOn() {
        return occurredOn;
    }
}