package com.juriba.tracker.auth.infrastructure.security;

import org.springframework.security.core.Authentication;

import java.time.Instant;

public interface JwtTokenProvider  {
    String generateAccessToken(Authentication authentication);
    String generateRefreshToken(Authentication authentication);
    Instant getRefreshTokenExpiration();
    Instant getAccessTokenExpiration();
    long getRefreshTokenExpirationInSeconds();
    long getAccessTokenExpirationInSeconds();



}
