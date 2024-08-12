package com.juriba.tracker.user.application;

import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.user.infrastructure.RoleRepository;
import com.juriba.tracker.user.presentation.dto.RoleResponse;
import com.juriba.tracker.user.presentation.mapper.RoleMapper;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class GetRoleUseCase {
    private final RoleRepository roleRepository;

    public GetRoleUseCase(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional(readOnly = true)
    public RoleResponse execute(String  id) {
        return roleRepository.findById(id).map(RoleMapper::toRoleResponse)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
    }
}