package com.juriba.tracker.audit.application.imp;

import com.juriba.tracker.audit.application.LogEventUseCase;
import com.juriba.tracker.audit.domain.AuditLog;
import com.juriba.tracker.audit.infrastructure.AuditLogRepository;
import com.juriba.tracker.common.application.DomainEvent;
import com.juriba.tracker.common.application.UseCase;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class LogEventUseCaseImp implements LogEventUseCase {
    private final AuditLogRepository auditLogRepository;

    public LogEventUseCaseImp(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    @Transactional
    public void execute(DomainEvent event, String entityId, String entityType, String action, String details, String performedBy) {
        AuditLog auditLog = AuditLog.builder()
                .eventType(event.getClass().getSimpleName())
                .entityId(entityId)
                .entityType(entityType)
                .action(action)
                .details(details)
                .occurredOn(event.occurredOn())
                .performedBy(performedBy)
                .build();

        auditLogRepository.save(auditLog);
    }
}
