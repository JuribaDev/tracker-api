package com.juriba.tracker.auth.domain;

import com.juriba.tracker.common.application.DomainEvent;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class AuthenticationAttemptedEvent implements DomainEvent {

    private final AuthenticationAttempt attempt;
    private final LocalDateTime occurredOn;
    public AuthenticationAttemptedEvent(AuthenticationAttempt attempt) {
        this.attempt = attempt;
        this.occurredOn = LocalDateTime.now();
    }



    @Override
    public LocalDateTime occurredOn() {
        return occurredOn;
    }
}