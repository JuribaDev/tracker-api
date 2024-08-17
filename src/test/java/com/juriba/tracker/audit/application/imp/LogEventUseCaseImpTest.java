package com.juriba.tracker.audit.application.imp;

import com.juriba.tracker.audit.domain.AuditLog;
import com.juriba.tracker.audit.infrastructure.AuditLogRepository;
import com.juriba.tracker.common.application.DomainEvent;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.OffsetDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class LogEventUseCaseImpTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    private LogEventUseCaseImp logEventUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        logEventUseCase = new LogEventUseCaseImp(auditLogRepository);
    }

    @Test
    void execute_shouldSaveAuditLog_whenValidEventProvided() {
        // Arrange
        DomainEvent event = new TestDomainEvent();
        String entityId = "entity123";
        String entityType = "TestEntity";
        String action = "Created";
        String details = "Test entity created";
        String performedBy = "user@example.com";

        // Act
        logEventUseCase.execute(event, entityId, entityType, action, details, performedBy);

        // Assert
        ArgumentCaptor<AuditLog> auditLogCaptor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogRepository).save(auditLogCaptor.capture());

        AuditLog savedAuditLog = auditLogCaptor.getValue();
        assertEquals("TestDomainEvent", savedAuditLog.getEventType());
        assertEquals(entityId, savedAuditLog.getEntityId());
        assertEquals(entityType, savedAuditLog.getEntityType());
        assertEquals(action, savedAuditLog.getAction());
        assertEquals(details, savedAuditLog.getDetails());
        assertEquals(performedBy, savedAuditLog.getPerformedBy());
        assertNotNull(savedAuditLog.getOccurredOn());
    }

    @Test
    void execute_shouldHandleNullDetails() {
        // Arrange
        DomainEvent event = new TestDomainEvent();
        String entityId = "entity123";
        String entityType = "TestEntity";
        String action = "Created";
        String performedBy = "user@example.com";

        // Act
        logEventUseCase.execute(event, entityId, entityType, action, null, performedBy);

        // Assert
        ArgumentCaptor<AuditLog> auditLogCaptor = ArgumentCaptor.forClass(AuditLog.class);
        verify(auditLogRepository).save(auditLogCaptor.capture());

        AuditLog savedAuditLog = auditLogCaptor.getValue();
        assertNull(savedAuditLog.getDetails());
    }

    private static class TestDomainEvent implements DomainEvent {
        @Override
        public OffsetDateTime occurredOn() {
            return OffsetDateTime.now();
        }
    }
}