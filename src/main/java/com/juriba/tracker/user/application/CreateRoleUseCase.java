package com.juriba.tracker.user.application;

import com.juriba.tracker.user.domain.Role;
import com.juriba.tracker.user.presentation.dto.RoleRequest;
import com.juriba.tracker.user.presentation.dto.RoleResponse;

public interface CreateRoleUseCase {
     RoleResponse execute(RoleRequest roleRequest);
}
