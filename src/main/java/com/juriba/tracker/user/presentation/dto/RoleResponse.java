package com.juriba.tracker.user.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.time.Instant;
import java.time.OffsetDateTime;

public record RoleResponse(
        String id,
        String name,
        @JsonProperty("created_at") Instant createdAt

) {
}
