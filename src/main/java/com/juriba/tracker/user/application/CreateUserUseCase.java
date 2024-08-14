package com.juriba.tracker.user.application;

import com.juriba.tracker.user.domain.User;
import com.juriba.tracker.user.presentation.dto.CreateUserRequest;

public interface CreateUserUseCase {
    User execute(CreateUserRequest createUserRequest);
}
