package com.juriba.tracker.auth.application;

import com.juriba.tracker.auth.presentation.AuthResponse;

public interface AuthService {
    AuthResponse authenticate(String email, String password);
}
