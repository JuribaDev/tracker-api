package com.juriba.tracker.auth.presentation.dto;

public record RegisterRequest(
        String email,
        String password,
        String name
) {
}
