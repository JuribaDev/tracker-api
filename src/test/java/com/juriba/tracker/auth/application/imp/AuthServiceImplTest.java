package com.juriba.tracker.auth.application.imp;

import com.juriba.tracker.auth.application.AuthenticationAttemptLogger;
import com.juriba.tracker.auth.infrastructure.security.JwtTokenProvider;
import com.juriba.tracker.auth.presentation.AuthResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AuthServiceImplTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private JwtTokenProvider jwtTokenProvider;

    @Mock
    private AuthenticationAttemptLogger authenticationAttemptLogger;

    @InjectMocks
    private AuthServiceImpl authService;

    @Test
    void authenticateSuccessful() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtTokenProvider.generateAccessToken(authentication)).thenReturn("access_token");
        when(jwtTokenProvider.generateRefreshToken(authentication)).thenReturn("refresh_token");
        when(jwtTokenProvider.getAccessTokenExpiration()).thenReturn(Instant.now().plusSeconds(3600));
        when(jwtTokenProvider.getRefreshTokenExpiration()).thenReturn(Instant.now().plusSeconds(86400));

        // Act
        AuthResponse response = authService.authenticate(email, password);

        // Assert
        assertNotNull(response);
        assertEquals("access_token", response.accessToken());
        assertEquals("refresh_token", response.refreshToken());
        verify(authenticationAttemptLogger).logSuccessfulAttempt(email);
    }

    @Test
    void authenticateFailure() {
        // Arrange
        String email = "test@example.com";
        String password = "wrong_password";
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        // Act/Assert
        assertThrows(BadCredentialsException.class, () -> authService.authenticate(email, password));
        verify(authenticationAttemptLogger).logFailedAttempt(email);
    }
}