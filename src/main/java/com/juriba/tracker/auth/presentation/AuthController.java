package com.juriba.tracker.auth.presentation;

import com.juriba.tracker.auth.application.imp.LoginUseCase;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/auth")
@Tag(name = "Auth Endpoints")
public class AuthController {
    private final LoginUseCase loginUseCase;

    public AuthController(LoginUseCase loginUseCase) {
        this.loginUseCase = loginUseCase;
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> authenticate(@Validated @RequestBody LoginRequest loginRequest) {
        AuthResponse authResponse = loginUseCase.execute(loginRequest);
        return ResponseEntity.ok(authResponse);
    }
}
