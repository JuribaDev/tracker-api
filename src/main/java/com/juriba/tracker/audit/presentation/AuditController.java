package com.juriba.tracker.audit.presentation;

import com.juriba.tracker.audit.application.GetAllAuditsUseCase;
import com.juriba.tracker.audit.presentation.dto.AuditResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.OffsetDateTime;


@RestController
@RequestMapping("/api/v1/audit")
@Tag(name = "Audit Endpoints")
public class AuditController {
    private final GetAllAuditsUseCase getAllAuditsUseCase;
    private final PagedResourcesAssembler<AuditResponse> pagedResourcesAssembler;

    public AuditController(GetAllAuditsUseCase getAllAuditsUseCase, PagedResourcesAssembler<AuditResponse> pagedResourcesAssembler) {
        this.getAllAuditsUseCase = getAllAuditsUseCase;
        this.pagedResourcesAssembler = pagedResourcesAssembler;
    }

    @GetMapping()
    public ResponseEntity<PagedModel<EntityModel<AuditResponse>>> getAllAudits(
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime startDate,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) OffsetDateTime endDate,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(defaultValue = "occurredOn") String sortBy,
            @RequestParam(defaultValue = "DESC") String sortDirection) {
        Page<AuditResponse> responses = getAllAuditsUseCase.execute(startDate, endDate, page, size, sortBy, sortDirection);
        return ResponseEntity.ok(pagedResourcesAssembler.toModel(responses));
    }


}
