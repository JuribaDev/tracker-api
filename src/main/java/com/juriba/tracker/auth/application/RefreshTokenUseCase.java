package com.juriba.tracker.auth.application;

import com.juriba.tracker.auth.presentation.dto.AuthResponse;

public interface RefreshTokenUseCase {
    AuthResponse execute(String refreshToken);
}
