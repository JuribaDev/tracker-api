package com.juriba.tracker.audit.application.imp;

import com.juriba.tracker.audit.domain.AuditLog;
import com.juriba.tracker.audit.infrastructure.AuditLogRepository;
import com.juriba.tracker.audit.presentation.dto.AuditResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import java.time.OffsetDateTime;
import java.util.Collections;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class GetAllAuditsUseCaseImplTest {

    @Mock
    private AuditLogRepository auditLogRepository;

    private GetAllAuditsUseCaseImpl getAllAuditsUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        getAllAuditsUseCase = new GetAllAuditsUseCaseImpl(auditLogRepository);
    }

    @Test
    void execute_shouldReturnPagedAudits_whenNoDateFiltersProvided() {
        // Arrange
        AuditLog auditLog = new AuditLog();
        auditLog.setId("audit123");
        auditLog.setEventType("UserCreated");
        auditLog.setEntityId("user123");
        auditLog.setEntityType("User");
        auditLog.setAction("Created");
        auditLog.setDetails("User john@example.com created");
        auditLog.setOccurredOn(OffsetDateTime.now());
        auditLog.setPerformedBy("system");

        Page<AuditLog> auditLogPage = new PageImpl<>(Collections.singletonList(auditLog));
        when(auditLogRepository.findAll(any(PageRequest.class))).thenReturn(auditLogPage);

        // Act
        Page<AuditResponse> result = getAllAuditsUseCase.execute(null, null, 0, 10, "occurredOn", "DESC");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        AuditResponse response = result.getContent().get(0);
        assertEquals("audit123", response.getId());
        assertEquals("UserCreated", response.getEventType());
        assertEquals("user123", response.getEntityId());
        assertEquals("User", response.getEntityType());
        assertEquals("Created", response.getAction());
        assertEquals("User john@example.com created", response.getDetails());
        assertNotNull(response.getOccurredOn());
        assertEquals("system", response.getPerformedBy());

        verify(auditLogRepository).findAll(any(PageRequest.class));
        verify(auditLogRepository, never()).findByOccurredOnBetween(any(), any(), any());
    }

    @Test
    void execute_shouldReturnFilteredAudits_whenDateFiltersProvided() {
        // Arrange
        OffsetDateTime startDate = OffsetDateTime.parse("2023-01-01T00:00:00Z");
        OffsetDateTime endDate = OffsetDateTime.parse("2023-12-31T23:59:59Z");

        AuditLog auditLog = new AuditLog();
        auditLog.setId("audit456");
        auditLog.setEventType("ExpenseCreated");
        auditLog.setOccurredOn(OffsetDateTime.parse("2023-06-15T12:30:00Z"));

        Page<AuditLog> auditLogPage = new PageImpl<>(Collections.singletonList(auditLog));
        when(auditLogRepository.findByOccurredOnBetween(eq(startDate), eq(endDate), any(PageRequest.class)))
                .thenReturn(auditLogPage);

        // Act
        Page<AuditResponse> result = getAllAuditsUseCase.execute(startDate, endDate, 0, 10, "occurredOn", "DESC");

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        AuditResponse response = result.getContent().get(0);
        assertEquals("audit456", response.getId());
        assertEquals("ExpenseCreated", response.getEventType());
        assertEquals(OffsetDateTime.parse("2023-06-15T12:30:00Z"), response.getOccurredOn());

        verify(auditLogRepository).findByOccurredOnBetween(eq(startDate), eq(endDate), any(PageRequest.class));
        verify(auditLogRepository, never()).findAll(any(PageRequest.class));
    }

    @Test
    void execute_shouldUseSortingParameters() {
        // Arrange
        when(auditLogRepository.findAll(any(PageRequest.class))).thenReturn(Page.empty());

        // Act
        getAllAuditsUseCase.execute(null, null, 0, 10, "eventType", "ASC");

        // Assert
        verify(auditLogRepository).findAll( PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "eventType")));
    }
}