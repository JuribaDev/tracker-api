package com.juriba.tracker.user.application;

import com.juriba.tracker.user.presentation.dto.RoleRequest;

public interface UpdateRoleUseCase {
    void execute(String id, RoleRequest newRole);
}
