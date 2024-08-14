package com.juriba.tracker.auth.presentation.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Builder;

import java.time.Instant;

@Builder
public record AuthResponse(
        @JsonProperty("access_token") String accessToken,
        @JsonProperty("refresh_token") String refreshToken,
        @JsonProperty("access_token_expiration") long accessTokenExpiration,
        @JsonProperty("refresh_token_expiration") long refreshTokenExpiration,
        @JsonProperty("expires_at") Instant expiresAt,
        @JsonProperty("refresh_expiration_at") Instant refreshExpiresAt) {
}
