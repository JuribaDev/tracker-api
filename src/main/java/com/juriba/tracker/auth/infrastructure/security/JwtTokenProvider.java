package com.juriba.tracker.auth.infrastructure.security;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final JwtEncoder encoder;

    @Value("${jwt.expiration.access:3600}")
    private long accessTokenExpiration;

    @Value("${jwt.expiration.refresh:86400}")
    private long refreshTokenExpiration;

    public JwtTokenProvider(JwtEncoder encoder) {
        this.encoder = encoder;
    }

    public String generateAccessToken(Authentication authentication) {
        return generateToken(authentication, accessTokenExpiration);
    }

    public String generateRefreshToken(Authentication authentication) {
        return generateToken(authentication, refreshTokenExpiration);
    }

    private String generateToken(Authentication authentication, long expirationTime) {
        Instant now = Instant.now();
        String scope = authentication.getAuthorities().stream()
                .map(GrantedAuthority::getAuthority)
                .collect(Collectors.joining(" "));
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("self")
                .issuedAt(now)
                .expiresAt(now.plus(expirationTime, ChronoUnit.SECONDS))
                .subject(authentication.getName())
                .claim("scope", scope)
                .build();
        return this.encoder.encode(JwtEncoderParameters.from(claims)).getTokenValue();
    }

    public Instant getAccessTokenExpiration() {
        return Instant.now().plus(accessTokenExpiration, ChronoUnit.SECONDS);
    }

    public Instant getRefreshTokenExpiration() {
        return Instant.now().plus(refreshTokenExpiration, ChronoUnit.SECONDS);
    }
}