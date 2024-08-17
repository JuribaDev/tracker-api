package com.juriba.tracker.audit.infrastructure;

import com.juriba.tracker.audit.domain.AuditLog;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class AuditLogRepositoryTest {

    @Autowired
    private AuditLogRepository auditLogRepository;

    @Test
    void findByOccurredOnBetween_shouldReturnAuditLogsWithinDateRange() {
        // Arrange
        OffsetDateTime now = OffsetDateTime.now();
        AuditLog log1 = createAuditLog(now.minus(2, ChronoUnit.DAYS));
        AuditLog log2 = createAuditLog(now.minus(1, ChronoUnit.DAYS));
        AuditLog log3 = createAuditLog(now);
        AuditLog log4 = createAuditLog(now.plus(1, ChronoUnit.DAYS));

        auditLogRepository.saveAll(List.of(log1, log2, log3, log4));

        OffsetDateTime startDate = now.minus(3, ChronoUnit.DAYS);
        OffsetDateTime endDate = now.plus(12, ChronoUnit.HOURS);

        // Act
        Page<AuditLog> result = auditLogRepository.findByOccurredOnBetween(
                startDate, endDate,
                PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, "occurredOn")));

        // Assert
        assertEquals(3, result.getTotalElements());
        List<AuditLog> content = result.getContent();
        assertEquals(log3.getId(), content.get(0).getId());
        assertEquals(log2.getId(), content.get(1).getId());
        assertEquals(log1.getId(), content.get(2).getId());
    }

    @Test
    void findAll_shouldReturnAllAuditLogsSortedByOccurredOn() {
        // Arrange
        OffsetDateTime now = OffsetDateTime.now();
        AuditLog log1 = createAuditLog(now.minus(2, ChronoUnit.DAYS));
        AuditLog log2 = createAuditLog(now.minus(1, ChronoUnit.DAYS));
        AuditLog log3 = createAuditLog(now);

        auditLogRepository.saveAll(List.of(log1, log2, log3));

        // Act
        Page<AuditLog> result = auditLogRepository.findAll(
                PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, "occurredOn")));

        // Assert
        assertEquals(3, result.getTotalElements());
        List<AuditLog> content = result.getContent();
        assertEquals(log1.getId(), content.get(0).getId());
        assertEquals(log2.getId(), content.get(1).getId());
        assertEquals(log3.getId(), content.get(2).getId());
    }

    private AuditLog createAuditLog(OffsetDateTime occurredOn) {
        AuditLog log = new AuditLog();
        log.setEventType("TestEvent");
        log.setEntityId("entity123");
        log.setEntityType("TestEntity");
        log.setAction("Created");
        log.setDetails("Test audit log");
        log.setOccurredOn(occurredOn);
        log.setPerformedBy("testUser");
        return log;
    }
}