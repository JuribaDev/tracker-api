package com.juriba.tracker.auth.application;

import com.juriba.tracker.user.domain.User;

public interface GetAuthenticatedUserUseCase {
    User execute();
}
