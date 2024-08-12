package com.juriba.tracker.auth.application.imp;


import com.juriba.tracker.auth.application.AuthenticationAttemptLogger;
import com.juriba.tracker.auth.domain.AuthenticationAttempt;
import com.juriba.tracker.auth.infrastructure.AuthenticationAttemptRepository;
import com.juriba.tracker.common.domain.EventPublisher;
import org.springframework.stereotype.Component;


@Component
public class AuthenticationAttemptLoggerImpl implements AuthenticationAttemptLogger {
    private final AuthenticationAttemptRepository attemptRepository;
    private final EventPublisher eventPublisher;

    public AuthenticationAttemptLoggerImpl(AuthenticationAttemptRepository attemptRepository,
                                           EventPublisher eventPublisher) {
        this.attemptRepository = attemptRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void logSuccessfulAttempt(String email) {
        logAttempt(email, true);
    }

    @Override
    public void logFailedAttempt(String email) {
        logAttempt(email, false);
    }

    private void logAttempt(String email, boolean successful) {
        AuthenticationAttempt attempt = new AuthenticationAttempt( email, successful);
        attemptRepository.save(attempt);
        attempt.getDomainEvents().forEach(eventPublisher::publish);
        attempt.clearDomainEvents();
    }
}
