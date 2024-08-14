package auth.usecase;

import com.juriba.tracker.auth.application.LogAuthenticationAttemptUseCase;
import com.juriba.tracker.auth.application.imp.LoginUseCaseImp;
import com.juriba.tracker.auth.application.imp.RefreshTokenUseCaseImp;
import com.juriba.tracker.auth.application.imp.RegisterUseCaseImp;
import com.juriba.tracker.auth.infrastructure.security.JwtTokenProvider;

import com.juriba.tracker.auth.presentation.dto.LoginRequest;
import com.juriba.tracker.auth.presentation.dto.AuthResponse;
import com.juriba.tracker.user.application.CreateUserUseCase;
import com.juriba.tracker.user.domain.User;
import com.juriba.tracker.user.presentation.dto.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtDecoder;

import java.time.Instant;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AuthUseCasesTests {

    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtTokenProvider jwtTokenProvider;
    @Mock private LogAuthenticationAttemptUseCase logAuthenticationAttemptUseCase;
    @Mock private CreateUserUseCase createUserUseCase;
    @Mock private JwtDecoder jwtDecoder;

    private LoginUseCaseImp loginUseCase;
    private RegisterUseCaseImp registerUseCase;
    private RefreshTokenUseCaseImp refreshTokenUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        loginUseCase = new LoginUseCaseImp(authenticationManager, jwtTokenProvider, logAuthenticationAttemptUseCase);
        registerUseCase = new RegisterUseCaseImp(createUserUseCase, authenticationManager, jwtTokenProvider);
        refreshTokenUseCase = new RefreshTokenUseCaseImp(jwtTokenProvider, jwtDecoder);
    }

    @Test
    void loginUseCase_SuccessfulLogin() {
        // Arrange
        LoginRequest loginRequest = new LoginRequest("test@example.com", "password");
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtTokenProvider.generateAccessToken(authentication)).thenReturn("access_token");
        when(jwtTokenProvider.generateRefreshToken(authentication)).thenReturn("refresh_token");
        when(jwtTokenProvider.getAccessTokenExpiration()).thenReturn(Instant.now().plusSeconds(3600));
        when(jwtTokenProvider.getRefreshTokenExpiration()).thenReturn(Instant.now().plusSeconds(86400));

        // Act
        AuthResponse response = loginUseCase.execute(loginRequest);

        // Assert
        assertNotNull(response);
        assertEquals("access_token", response.accessToken());
        assertEquals("refresh_token", response.refreshToken());
        verify(logAuthenticationAttemptUseCase).execute(loginRequest.email(), true);
    }

    @Test
    void registerUseCase_SuccessfulRegistration() {
        // Arrange
        CreateUserRequest registerRequest = new CreateUserRequest("test@example.com", "password", "Test User");
        User createdUser = new User("Test User", "test@example.com", "encoded_password");
        when(createUserUseCase.execute(registerRequest)).thenReturn(createdUser);
        Authentication authentication = mock(Authentication.class);
        when(authenticationManager.authenticate(any())).thenReturn(authentication);
        when(jwtTokenProvider.generateAccessToken(authentication)).thenReturn("access_token");
        when(jwtTokenProvider.generateRefreshToken(authentication)).thenReturn("refresh_token");
        when(jwtTokenProvider.getAccessTokenExpiration()).thenReturn(Instant.now().plusSeconds(3600));
        when(jwtTokenProvider.getRefreshTokenExpiration()).thenReturn(Instant.now().plusSeconds(86400));

        // Act
        AuthResponse response = registerUseCase.execute(registerRequest);

        // Assert
        assertNotNull(response);
        assertEquals("access_token", response.accessToken());
        assertEquals("refresh_token", response.refreshToken());
    }

    @Test
    void refreshTokenUseCase_SuccessfulRefresh() {
        // Arrange
        String refreshToken = "valid_refresh_token";
        Jwt jwt = mock(Jwt.class);
        when(jwtDecoder.decode(refreshToken)).thenReturn(jwt);
        when(jwt.getSubject()).thenReturn("test@example.com");
        when(jwt.getClaimAsStringList("scope")).thenReturn(Collections.singletonList("USER"));
        when(jwtTokenProvider.generateAccessToken(any())).thenReturn("new_access_token");
        when(jwtTokenProvider.generateRefreshToken(any())).thenReturn("new_refresh_token");
        when(jwtTokenProvider.getAccessTokenExpiration()).thenReturn(Instant.now().plusSeconds(3600));
        when(jwtTokenProvider.getRefreshTokenExpiration()).thenReturn(Instant.now().plusSeconds(86400));

        // Act
        AuthResponse response = refreshTokenUseCase.execute(refreshToken);

        // Assert
        assertNotNull(response);
        assertEquals("new_access_token", response.accessToken());
        assertEquals("new_refresh_token", response.refreshToken());
    }
}