package com.juriba.tracker.user.application;

import com.juriba.tracker.user.domain.Role;
import com.juriba.tracker.user.presentation.dto.RoleRequest;

public interface CreateRoleUseCase {
     Role execute(RoleRequest roleRequest);
}
