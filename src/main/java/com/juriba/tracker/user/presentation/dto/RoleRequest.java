package com.juriba.tracker.user.presentation.dto;

import jakarta.validation.constraints.NotNull;

public record RoleRequest(@NotNull(message = "Role name is required") String name) {
}
