package audit.usecase;


import com.juriba.tracker.auth.application.LogAuthenticationAttemptUseCase;
import com.juriba.tracker.auth.application.imp.LogAuthenticationAttemptUseCaseImpl;
import com.juriba.tracker.auth.domain.event.AuthenticationAttemptedEvent;
import com.juriba.tracker.auth.infrastructure.AuthenticationAttemptRepository;
import com.juriba.tracker.common.domain.EventPublisher;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

class LoginAttemptsUseCasesTests {

    @Mock
    private AuthenticationAttemptRepository attemptRepository;

    @Mock
    private EventPublisher eventPublisher;

    private LogAuthenticationAttemptUseCase logAuthenticationAttemptUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        logAuthenticationAttemptUseCase = new LogAuthenticationAttemptUseCaseImpl(attemptRepository, eventPublisher);
    }

    @Test
    void logAuthenticationAttemptUseCase_SuccessfulAttempt() {
        // Arrange
        String email = "test@example.com";
        boolean isSuccessful = true;

        // Act
        logAuthenticationAttemptUseCase.execute(email, isSuccessful);

        // Assert
        verify(attemptRepository).save(argThat(attempt ->
                attempt.getEmail().equals(email) &&
                        attempt.isSuccessful() == isSuccessful
        ));
        verify(eventPublisher).publish(any());
    }

    @Test
    void logAuthenticationAttemptUseCase_FailedAttempt() {
        // Arrange
        String email = "test@example.com";
        boolean isSuccessful = false;

        // Act
        logAuthenticationAttemptUseCase.execute(email, isSuccessful);

        // Assert
        verify(attemptRepository).save(argThat(attempt ->
                attempt.getEmail().equals(email) &&
                        attempt.isSuccessful() == isSuccessful
        ));
        verify(eventPublisher).publish(any());
    }

    @Test
    void logAuthenticationAttemptUseCase_EventPublished() {
        // Arrange
        String email = "test@example.com";
        boolean isSuccessful = true;

        // Act
        logAuthenticationAttemptUseCase.execute(email, isSuccessful);

        // Assert
        verify(eventPublisher).publish(argThat(event ->
                event instanceof AuthenticationAttemptedEvent &&
                        ((AuthenticationAttemptedEvent) event).getAttempt().getEmail().equals(email) &&
                        ((AuthenticationAttemptedEvent) event).getAttempt().isSuccessful() == isSuccessful
        ));
    }
}
