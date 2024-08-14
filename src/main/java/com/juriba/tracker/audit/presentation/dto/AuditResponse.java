package com.juriba.tracker.audit.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.juriba.tracker.expense.presentation.dto.ExpenseResponse;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.springframework.hateoas.RepresentationModel;

import java.time.OffsetDateTime;

@EqualsAndHashCode(callSuper = true)
@Data
public class AuditResponse extends RepresentationModel<ExpenseResponse> {
    private String id;
    @JsonProperty("event_type")
    private String eventType;
    @JsonProperty("event_id")
    private String entityId;
    @JsonProperty("entity_type")
    private String entityType;
    private String action;
    private String details;
    @JsonProperty("occurred_on")
    private OffsetDateTime occurredOn;
    @JsonProperty("performed_by")
    private String performedBy;
}
