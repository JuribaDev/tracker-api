package com.juriba.tracker.user.presentation.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotNull;

public record CreateUserRequest(
        @Email(message = "Invalid email")
        @NotNull(message = "Email is required")
        String email,
        @NotNull(message = "Password is required")
        String password,
        @NotNull(message = "Name is required")
        String name

) {
}
