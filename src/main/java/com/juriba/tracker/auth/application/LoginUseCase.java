package com.juriba.tracker.auth.application;

import com.juriba.tracker.auth.presentation.dto.AuthResponse;
import com.juriba.tracker.auth.presentation.dto.LoginRequest;

public interface LoginUseCase {
    AuthResponse execute(LoginRequest loginRequest);
}
