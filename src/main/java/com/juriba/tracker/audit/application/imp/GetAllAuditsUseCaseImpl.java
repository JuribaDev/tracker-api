package com.juriba.tracker.audit.application.imp;

import com.juriba.tracker.audit.application.GetAllAuditsUseCase;
import com.juriba.tracker.audit.domain.AuditLog;
import com.juriba.tracker.audit.infrastructure.AuditLogRepository;
import com.juriba.tracker.audit.presentation.dto.AuditResponse;
import com.juriba.tracker.audit.presentation.mapper.AuditMapper;
import com.juriba.tracker.common.application.UseCase;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;

@UseCase
public class GetAllAuditsUseCaseImpl implements GetAllAuditsUseCase {
    private final AuditLogRepository auditLogRepository;

    public GetAllAuditsUseCaseImpl(AuditLogRepository auditLogRepository) {
        this.auditLogRepository = auditLogRepository;
    }

    @Override
    @Transactional(readOnly = true)
    public Page<AuditResponse> execute(Instant startDate, Instant endDate, int page, int size, String sortBy, String sortDirection) {
        Sort sort = Sort.by(Sort.Direction.fromString(sortDirection), sortBy);
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<AuditLog> auditLogs;
        if (startDate != null && endDate != null) {
            auditLogs = auditLogRepository.findByOccurredOnBetween(startDate, endDate, pageable);
        } else {
            auditLogs = auditLogRepository.findAll(pageable);
        }

        return auditLogs.map(AuditMapper::toAuditResponse);
    }
}
