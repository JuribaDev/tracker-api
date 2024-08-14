package com.juriba.tracker.audit.presentation.mapper;


import com.juriba.tracker.audit.domain.AuditLog;
import com.juriba.tracker.audit.presentation.AuditController;
import com.juriba.tracker.audit.presentation.dto.AuditResponse;
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder;

public class AuditMapper {
    public static AuditResponse toAuditResponse(AuditLog auditLog) {
        AuditResponse response = new AuditResponse();
        response.setId(auditLog.getId());
        response.setEventType(auditLog.getEventType());
        response.setEntityId(auditLog.getEntityId());
        response.setEntityType(auditLog.getEntityType());
        response.setAction(auditLog.getAction());
        response.setDetails(auditLog.getDetails());
        response.setOccurredOn(auditLog.getOccurredOn());
        response.setPerformedBy(auditLog.getPerformedBy());

        response.add(WebMvcLinkBuilder.linkTo(
                        WebMvcLinkBuilder.methodOn(AuditController.class)
                                .getAllAudits(null, null, 0, 10, "occurredOn", "DESC"))
                .withRel("audits"));

        return response;
    }
}
