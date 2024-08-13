package com.juriba.tracker.auth.application.imp;


import com.juriba.tracker.auth.application.LogAuthenticationAttemptUseCase;
import com.juriba.tracker.auth.domain.AuthenticationAttempt;
import com.juriba.tracker.auth.infrastructure.AuthenticationAttemptRepository;
import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.common.domain.EventPublisher;


@UseCase
public class LogAuthenticationAttemptUseCaseImpl implements LogAuthenticationAttemptUseCase {
    private final AuthenticationAttemptRepository attemptRepository;
    private final EventPublisher eventPublisher;

    public LogAuthenticationAttemptUseCaseImpl(AuthenticationAttemptRepository attemptRepository,
                                               EventPublisher eventPublisher) {
        this.attemptRepository = attemptRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    public void execute(String email, boolean isSuccessful) {
        AuthenticationAttempt attempt = new AuthenticationAttempt( email, isSuccessful);
        attemptRepository.save(attempt);
        attempt.getDomainEvents().forEach(eventPublisher::publish);
        attempt.clearDomainEvents();

    }
}
