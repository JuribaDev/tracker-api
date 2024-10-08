package com.juriba.tracker.audit.application;

import com.juriba.tracker.audit.presentation.dto.AuditResponse;
import org.springframework.data.domain.Page;

import java.time.OffsetDateTime;

public interface GetAllAuditsUseCase {
    Page<AuditResponse> execute(OffsetDateTime startDate, OffsetDateTime endDate, int page, int size, String sortBy, String sortDirection);
}
