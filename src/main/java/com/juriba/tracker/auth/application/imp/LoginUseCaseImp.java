package com.juriba.tracker.auth.application.imp;

import com.juriba.tracker.auth.application.LogAuthenticationAttemptUseCase;
import com.juriba.tracker.auth.application.LoginUseCase;
import com.juriba.tracker.auth.infrastructure.security.imp.JwtTokenProviderImp;
import com.juriba.tracker.auth.presentation.AuthResponse;
import com.juriba.tracker.auth.presentation.LoginRequest;
import com.juriba.tracker.common.application.UseCase;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;


@UseCase
public class LoginUseCaseImp implements LoginUseCase {
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProviderImp jwtTokenProvider;
    private final LogAuthenticationAttemptUseCase logAuthenticationAttemptUseCase;

    public LoginUseCaseImp(AuthenticationManager authenticationManager,
                           JwtTokenProviderImp jwtTokenProvider,
                           LogAuthenticationAttemptUseCase logAuthenticationAttemptUseCase) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
        this.logAuthenticationAttemptUseCase = logAuthenticationAttemptUseCase;
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
            logAuthenticationAttemptUseCase.execute(loginRequest.email(), true);
            return authResponse;
        } catch (Exception e) {
            logAuthenticationAttemptUseCase.execute(loginRequest.email(), false);
            throw e;
        }
    }
}