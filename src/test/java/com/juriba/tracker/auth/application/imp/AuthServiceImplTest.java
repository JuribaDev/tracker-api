package com.juriba.tracker.auth.application.imp;

import com.juriba.tracker.auth.application.LogAuthenticationAttemptUseCase;
import com.juriba.tracker.auth.infrastructure.security.imp.JwtTokenProviderImp;
import com.juriba.tracker.auth.presentation.AuthResponse;
import com.juriba.tracker.auth.presentation.LoginRequest;
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
    private JwtTokenProviderImp jwtTokenProvider;

    @Mock
    private LogAuthenticationAttemptUseCase logAuthenticationAttemptUseCase;

    @InjectMocks
    private LoginUseCaseImp loginUseCaseImp;

    @Test
    void authenticateSuccessful() {
        // Arrange
        String email = "test@example.com";
        String password = "password";
        LoginRequest loginRequest = new LoginRequest(email, password);
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtTokenProvider.generateAccessToken(authentication)).thenReturn("access_token");
        when(jwtTokenProvider.generateRefreshToken(authentication)).thenReturn("refresh_token");
        when(jwtTokenProvider.getAccessTokenExpiration()).thenReturn(Instant.now().plusSeconds(3600));
        when(jwtTokenProvider.getRefreshTokenExpiration()).thenReturn(Instant.now().plusSeconds(86400));

        // Act
        AuthResponse response = loginUseCaseImp.execute(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("access_token", response.accessToken());
        assertEquals("refresh_token", response.refreshToken());
        verify(logAuthenticationAttemptUseCase).execute(email, true);
    }

    @Test
    void authenticateFailure() {
        // Arrange
        String email = "test@example.com";
        String password = "wrong_password";
        LoginRequest loginRequest = new LoginRequest(email, password);
        when(authenticationManager.authenticate(any())).thenThrow(new BadCredentialsException("Bad credentials"));

        // Act/Assert
        assertThrows(BadCredentialsException.class, () -> loginUseCaseImp.execute(loginRequest));
        verify(logAuthenticationAttemptUseCase).execute(email,false);
    }

}