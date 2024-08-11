package com.juriba.tracker.auth.application.imp;

import com.juriba.tracker.auth.application.AuthService;
import com.juriba.tracker.auth.application.AuthenticationAttemptLogger;
import com.juriba.tracker.auth.infrastructure.security.JwtTokenProvider;
import com.juriba.tracker.auth.presentation.AuthResponse;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


@Service
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;
    private final AuthenticationAttemptLogger authenticationAttemptLogger;

    public AuthServiceImpl(AuthenticationManager authenticationManager,
                           JwtTokenProvider jwtTokenProvider,
                           AuthenticationAttemptLogger authenticationAttemptLogger) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.authenticationAttemptLogger = authenticationAttemptLogger;
    }

    @Override
    @Transactional
    public AuthResponse authenticate(String email, String password) {
        try {
            Authentication authentication = authenticationManager.authenticate(
                    new UsernamePasswordAuthenticationToken(email, password)
            );

            authenticationAttemptLogger.logSuccessfulAttempt(email);

            String accessToken = jwtTokenProvider.generateAccessToken(authentication);
            String refreshToken = jwtTokenProvider.generateRefreshToken(authentication);

            return new AuthResponse(
                    accessToken,
                    refreshToken,
                    jwtTokenProvider.getAccessTokenExpiration(),
                    jwtTokenProvider.getRefreshTokenExpiration()
            );
        } catch (Exception e) {
            authenticationAttemptLogger.logFailedAttempt(email);
            throw e;
        }
    }
}