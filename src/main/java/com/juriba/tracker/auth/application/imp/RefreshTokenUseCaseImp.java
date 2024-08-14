package com.juriba.tracker.auth.application.imp;

import com.juriba.tracker.auth.application.RefreshTokenUseCase;
import com.juriba.tracker.auth.infrastructure.security.JwtTokenProvider;
import com.juriba.tracker.auth.presentation.dto.AuthResponse;
import com.juriba.tracker.auth.infrastructure.security.imp.JwtTokenProviderImp;
import com.juriba.tracker.common.application.UseCase;
import org.springframework.security.core.Authentication;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@UseCase
public class RefreshTokenUseCaseImp implements RefreshTokenUseCase {

    private final JwtTokenProvider jwtTokenProvider;
    private final JwtDecoder jwtDecoder;

    public RefreshTokenUseCaseImp(JwtTokenProvider jwtTokenProvider, JwtDecoder jwtDecoder) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.jwtDecoder = jwtDecoder;
    }

    @Override
    @Transactional
    public AuthResponse execute(String refreshToken) {
        if (refreshToken == null || refreshToken.isEmpty()) {
            throw new IllegalArgumentException("Refresh token header or request is required");
        }

        try {
            var jwt = jwtDecoder.decode(refreshToken);
            String username = jwt.getSubject();
            List<String> scope = jwt.getClaimAsStringList("scope");

            Authentication authentication = new UsernamePasswordAuthenticationToken(username, null,
                    scope.stream().map(authority -> (GrantedAuthority) () -> authority).toList());

            return AuthResponse.builder()
                    .accessToken(jwtTokenProvider.generateAccessToken(authentication))
                    .refreshToken(jwtTokenProvider.generateRefreshToken(authentication))
                    .accessTokenExpiration(jwtTokenProvider.getAccessTokenExpirationInSeconds())
                    .refreshTokenExpiration(jwtTokenProvider.getRefreshTokenExpirationInSeconds())
                    .expiresAt(jwtTokenProvider.getAccessTokenExpiration())
                    .refreshExpiresAt(jwtTokenProvider.getRefreshTokenExpiration())
                    .build();
        } catch (JwtException e) {
            throw new IllegalArgumentException("Invalid refresh token", e);
        }
    }
}
