package com.juriba.tracker.auth.application.imp;

import com.juriba.tracker.auth.application.LogAuthenticationAttemptUseCase;
import com.juriba.tracker.auth.infrastructure.security.JwtTokenProvider;
import com.juriba.tracker.auth.presentation.dto.AuthResponse;
import com.juriba.tracker.auth.presentation.dto.LoginRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class LoginUseCaseImpTest {

    @Mock
    private AuthenticationManager authenticationManager;
    @Mock
    private JwtTokenProvider jwtTokenProvider;
    @Mock
    private LogAuthenticationAttemptUseCase logAuthenticationAttemptUseCase;

    private LoginUseCaseImp loginUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loginUseCase = new LoginUseCaseImp(authenticationManager, jwtTokenProvider, logAuthenticationAttemptUseCase);
    }

    @Test
    void execute_shouldReturnAuthResponse_whenValidCredentials() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("user@example.com", "password");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class))).thenReturn(authentication);
        when(jwtTokenProvider.generateAccessToken(authentication)).thenReturn("access_token");
        when(jwtTokenProvider.generateRefreshToken(authentication)).thenReturn("refresh_token");
        when(jwtTokenProvider.getAccessTokenExpirationInSeconds()).thenReturn(3600L);
        when(jwtTokenProvider.getRefreshTokenExpirationInSeconds()).thenReturn(86400L);
        when(jwtTokenProvider.getAccessTokenExpiration()).thenReturn(Instant.now().plusSeconds(3600));
        when(jwtTokenProvider.getRefreshTokenExpiration()).thenReturn(Instant.now().plusSeconds(86400));

        // Act
        AuthResponse response = loginUseCase.execute(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("access_token", response.accessToken());
        assertEquals("refresh_token", response.refreshToken());
        assertEquals(3600L, response.accessTokenExpiration());
        assertEquals(86400L, response.refreshTokenExpiration());

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtTokenProvider).generateAccessToken(authentication);
        verify(jwtTokenProvider).generateRefreshToken(authentication);
        verify(logAuthenticationAttemptUseCase).execute("user@example.com", true);
    }

    @Test
    void execute_shouldThrowException_whenInvalidCredentials() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("user@example.com", "wrong_password");
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Invalid credentials"));

        // Act & Assert
        assertThrows(BadCredentialsException.class, () -> loginUseCase.execute(loginRequest));

        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(logAuthenticationAttemptUseCase).execute("user@example.com", false);
        verifyNoInteractions(jwtTokenProvider);
    }
}