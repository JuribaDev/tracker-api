package com.juriba.tracker.auth.application.imp;

import com.juriba.tracker.auth.infrastructure.security.JwtTokenProvider;
import com.juriba.tracker.auth.presentation.dto.AuthResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;

import java.time.Instant;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class RefreshTokenUseCaseImpTest {

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private JwtDecoder jwtDecoder;

    private RefreshTokenUseCaseImp refreshTokenUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        refreshTokenUseCase = new RefreshTokenUseCaseImp(jwtTokenProvider, jwtDecoder);
    }

    @Test
    void execute_shouldReturnNewTokens_whenValidRefreshToken() {
        // Arrange
        String refreshToken = "valid.refresh.token";
        Jwt decodedJwt = mock(Jwt.class);
        when(decodedJwt.getSubject()).thenReturn("user@example.com");
        when(decodedJwt.getClaimAsStringList("scope")).thenReturn(Collections.singletonList("ROLE_USER"));
        when(jwtDecoder.decode(refreshToken)).thenReturn(decodedJwt);

        when(jwtTokenProvider.generateAccessToken(any(Authentication.class))).thenReturn("new.access.token");
        when(jwtTokenProvider.generateRefreshToken(any(Authentication.class))).thenReturn("new.refresh.token");
        when(jwtTokenProvider.getAccessTokenExpirationInSeconds()).thenReturn(3600L);
        when(jwtTokenProvider.getRefreshTokenExpirationInSeconds()).thenReturn(86400L);
        when(jwtTokenProvider.getAccessTokenExpiration()).thenReturn(Instant.now().plusSeconds(3600));
        when(jwtTokenProvider.getRefreshTokenExpiration()).thenReturn(Instant.now().plusSeconds(86400));

        // Act
        AuthResponse response = refreshTokenUseCase.execute(refreshToken);

        // Assert
        assertNotNull(response);
        assertEquals("new.access.token", response.accessToken());
        assertEquals("new.refresh.token", response.refreshToken());
        assertEquals(3600L, response.accessTokenExpiration());
        assertEquals(86400L, response.refreshTokenExpiration());
        assertNotNull(response.expiresAt());
        assertNotNull(response.refreshExpiresAt());

        verify(jwtDecoder).decode(refreshToken);
        verify(jwtTokenProvider).generateAccessToken(any(Authentication.class));
        verify(jwtTokenProvider).generateRefreshToken(any(Authentication.class));
    }

    @Test
    void execute_shouldThrowException_whenInvalidRefreshToken() {
        // Arrange
        String invalidRefreshToken = "invalid.refresh.token";
        when(jwtDecoder.decode(invalidRefreshToken)).thenThrow(new JwtException("Invalid token"));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> refreshTokenUseCase.execute(invalidRefreshToken));
        verify(jwtDecoder).decode(invalidRefreshToken);
        verifyNoInteractions(jwtTokenProvider);
    }

    @Test
    void execute_shouldThrowException_whenRefreshTokenIsNull() {
        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> refreshTokenUseCase.execute(null));
        verifyNoInteractions(jwtDecoder);
        verifyNoInteractions(jwtTokenProvider);
    }
}