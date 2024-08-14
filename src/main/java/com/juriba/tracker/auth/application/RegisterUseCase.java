package com.juriba.tracker.auth.application;

import com.juriba.tracker.auth.presentation.dto.AuthResponse;
import com.juriba.tracker.user.presentation.dto.CreateUserRequest;

public interface RegisterUseCase {
    AuthResponse execute(CreateUserRequest registerRequest);
}
