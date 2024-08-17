package com.juriba.tracker.user.presentation.mapper;

import com.juriba.tracker.user.domain.Role;
import com.juriba.tracker.user.presentation.dto.RoleResponse;

public class RoleMapper {
    public static RoleResponse toRoleResponse(Role role) {
        return new RoleResponse(
                role.getId(),
                role.getName(),
                role.getCreatedAt(),
                role.getModifiedAt()
        );
    }
}
