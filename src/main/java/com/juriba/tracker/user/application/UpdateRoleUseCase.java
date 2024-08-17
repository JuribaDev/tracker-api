package com.juriba.tracker.user.application;

import com.juriba.tracker.user.presentation.dto.RoleRequest;
import com.juriba.tracker.user.presentation.dto.RoleResponse;

public interface UpdateRoleUseCase {
    RoleResponse execute(String id, RoleRequest newRole);
}
