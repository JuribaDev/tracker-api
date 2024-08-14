package com.juriba.tracker.auth.application.imp;


import com.juriba.tracker.auth.application.RegisterUseCase;
import com.juriba.tracker.auth.presentation.dto.AuthResponse;
import com.juriba.tracker.auth.infrastructure.security.JwtTokenProvider;
import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.user.application.CreateUserUseCase;
import com.juriba.tracker.user.domain.User;
import com.juriba.tracker.user.presentation.dto.CreateUserRequest;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class RegisterUseCaseImp implements RegisterUseCase {

    private final CreateUserUseCase createUserUseCase;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider jwtTokenProvider;

    public RegisterUseCaseImp(CreateUserUseCase createUserUseCase,
                              AuthenticationManager authenticationManager,
                              JwtTokenProvider jwtTokenProvider) {
        this.createUserUseCase = createUserUseCase;
        this.authenticationManager = authenticationManager;
        this.jwtTokenProvider = jwtTokenProvider;
    }

    @Override
    @Transactional
    public AuthResponse execute(CreateUserRequest registerRequest) {


        User savedUser = createUserUseCase.execute(registerRequest);

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(savedUser.getEmail(), registerRequest.password())
        );

        return AuthResponse.builder()
                .accessToken(jwtTokenProvider.generateAccessToken(authentication))
                .refreshToken(jwtTokenProvider.generateRefreshToken(authentication))
                .accessTokenExpiration(jwtTokenProvider.getAccessTokenExpirationInSeconds())
                .refreshTokenExpiration(jwtTokenProvider.getRefreshTokenExpirationInSeconds())
                .expiresAt(jwtTokenProvider.getAccessTokenExpiration())
                .refreshExpiresAt(jwtTokenProvider.getRefreshTokenExpiration())
                .build();
    }
}
