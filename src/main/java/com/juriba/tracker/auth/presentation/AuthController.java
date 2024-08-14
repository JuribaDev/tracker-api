package com.juriba.tracker.auth.presentation;

import com.juriba.tracker.auth.application.RefreshTokenUseCase;
import com.juriba.tracker.auth.application.RegisterUseCase;
import com.juriba.tracker.auth.application.LoginUseCase;
import com.juriba.tracker.auth.presentation.dto.AuthResponse;
import com.juriba.tracker.auth.presentation.dto.LoginRequest;
import com.juriba.tracker.auth.presentation.dto.RefreshTokenRequest;
import com.juriba.tracker.user.presentation.dto.CreateUserRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth Endpoints")
public class AuthController {
    private final LoginUseCase loginUseCase;
    private final RefreshTokenUseCase refreshTokenUseCase;
    private final RegisterUseCase registerUseCase;

    public AuthController(LoginUseCase loginUseCase, RefreshTokenUseCase refreshTokenUseCase, RegisterUseCase registerUseCase) {
        this.loginUseCase = loginUseCase;
        this.refreshTokenUseCase = refreshTokenUseCase;
        this.registerUseCase = registerUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@Validated @RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = loginUseCase.execute(loginRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@Validated @RequestBody CreateUserRequest registerRequest) {
        AuthResponse authResponse = registerUseCase.execute(registerRequest);
        return ResponseEntity.ok(authResponse);
    }

    @PostMapping("/refresh-token")
    @Operation(parameters = {
            @Parameter(ref = "#/components/parameters/Refresh-Token")
    })
    public ResponseEntity<AuthResponse> refresh(
            @RequestHeader(value = "Refresh-Token",required = false) String refreshToken,
            @RequestBody RefreshTokenRequest refreshTokenRequest) {

        AuthResponse authResponse = refreshTokenUseCase.execute(refreshToken != null ? refreshToken : refreshTokenRequest.refreshToken());
        return ResponseEntity.ok(authResponse);
    }

}
