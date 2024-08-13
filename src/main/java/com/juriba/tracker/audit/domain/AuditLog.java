package com.juriba.tracker.audit.domain;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;

@Entity
@Table(name = "audit_logs")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AuditLog {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @Column(nullable = false)
    private String eventType;

    @Column(nullable = false)
    private String entityId;

    @Column(nullable = false)
    private String entityType;

    @Column(nullable = false)
    private String action;

    @Column(columnDefinition = "TEXT")
    private String details;

    @Column(nullable = false)
    private OffsetDateTime occurredOn;

    @Column(nullable = false)
    private String performedBy;
}
