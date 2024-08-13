package com.juriba.tracker.auth.application;

import com.juriba.tracker.auth.presentation.AuthResponse;
import com.juriba.tracker.auth.presentation.LoginRequest;

public interface AuthService {
    AuthResponse authenticate(LoginRequest loginRequest);
}
