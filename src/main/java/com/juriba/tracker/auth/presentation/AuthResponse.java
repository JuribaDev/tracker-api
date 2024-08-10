package com.juriba.tracker.auth.presentation;

import java.time.Instant;

public record AuthResponse(String accessToken, String refreshToken, Instant expiresAt, Instant refreshExpiresAt) {
}
