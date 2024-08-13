package com.juriba.tracker.audit.application;

import com.juriba.tracker.common.application.DomainEvent;

public interface LogEventUseCase {
     void execute(DomainEvent event, String entityId, String entityType, String action, String details, String performedBy);
}
