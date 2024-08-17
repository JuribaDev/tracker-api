package com.juriba.tracker.audit.infrastructure;


import com.juriba.tracker.audit.domain.AuditLog;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.OffsetDateTime;

@Repository
public interface AuditLogRepository extends JpaRepository<AuditLog, String> {
    Page<AuditLog> findByOccurredOnBetween(OffsetDateTime startDate, OffsetDateTime endDate, Pageable pageable);
}