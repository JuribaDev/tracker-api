package com.juriba.tracker.user.application;

import com.juriba.tracker.user.presentation.dto.RoleResponse;

public interface GetRoleUseCase {
    RoleResponse execute(String id);
}
