package audit.usecase;


import com.juriba.tracker.audit.application.LogEventUseCase;
import com.juriba.tracker.audit.application.imp.LogEventUseCaseImp;
import com.juriba.tracker.audit.infrastructure.AuditLogRepository;
import com.juriba.tracker.common.application.DomainEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.OffsetDateTime;

import static org.mockito.Mockito.*;

class AuditUseCasesTests {

    @Mock
    private AuditLogRepository auditLogRepository;

    private LogEventUseCase logEventUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        logEventUseCase = new LogEventUseCaseImp(auditLogRepository);
    }

    @Test
    void logEventUseCase_SuccessfulLogging() {
        // Arrange
        DomainEvent mockEvent = new DomainEvent() {
            @Override
            public OffsetDateTime occurredOn() {
                return OffsetDateTime.now();
            }
        };
        String entityId = "entity123";
        String entityType = "User";
        String action = "Created";
        String details = "User created with email: test@example.com";
        String performedBy = "system";

        // Act
        logEventUseCase.execute(mockEvent, entityId, entityType, action, details, performedBy);

        // Assert
        verify(auditLogRepository).save(argThat(auditLog ->
                auditLog.getEventType().equals(mockEvent.getClass().getSimpleName()) &&
                        auditLog.getEntityId().equals(entityId) &&
                        auditLog.getEntityType().equals(entityType) &&
                        auditLog.getAction().equals(action) &&
                        auditLog.getDetails().equals(details) &&
                        auditLog.getPerformedBy().equals(performedBy)
        ));
    }
}