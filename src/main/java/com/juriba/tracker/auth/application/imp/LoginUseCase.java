package com.juriba.tracker.auth.application.imp;

import com.juriba.tracker.auth.application.AuthenticationAttemptLogger;
import com.juriba.tracker.auth.infrastructure.security.imp.JwtTokenProviderImp;
import com.juriba.tracker.auth.presentation.AuthResponse;
import com.juriba.tracker.auth.presentation.LoginRequest;
import com.juriba.tracker.common.application.UseCase;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;


@UseCase
public class LoginUseCase {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProviderImp jwtTokenProvider;
    private final AuthenticationAttemptLogger authenticationAttemptLogger;

    public LoginUseCase(AuthenticationManager authenticationManager,
                        JwtTokenProviderImp jwtTokenProvider,
                        AuthenticationAttemptLogger authenticationAttemptLogger) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationAttemptLogger = authenticationAttemptLogger;
    }

    @Transactional
    public AuthResponse execute(LoginRequest loginRequest) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(loginRequest.email(), loginRequest.password())
            );
            AuthResponse authResponse = AuthResponse.builder()
                    .accessToken(jwtTokenProvider.generateAccessToken(authentication))
                    .refreshToken(jwtTokenProvider.generateRefreshToken(authentication))
                    .accessTokenExpiration(jwtTokenProvider.getAccessTokenExpirationInSeconds())
                    .refreshTokenExpiration(jwtTokenProvider.getRefreshTokenExpirationInSeconds())
                    .expiresAt(jwtTokenProvider.getRefreshTokenExpiration())
                    .refreshExpiresAt(jwtTokenProvider.getAccessTokenExpiration())
                    .build();
            authenticationAttemptLogger.logSuccessfulAttempt(loginRequest.email());
            return authResponse;
        } catch (Exception e) {
            authenticationAttemptLogger.logFailedAttempt(loginRequest.email());
            throw e;
        }
    }
}