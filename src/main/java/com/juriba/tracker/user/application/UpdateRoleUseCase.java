package com.juriba.tracker.user.application;

import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.user.domain.Role;
import com.juriba.tracker.user.infrastructure.RoleRepository;
import com.juriba.tracker.user.presentation.dto.RoleRequest;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class UpdateRoleUseCase {
    private final RoleRepository roleRepository;

    public UpdateRoleUseCase(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void execute(String id, RoleRequest newRole) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        if (!role.getName().equals(newRole.name()) && roleRepository.existsByName(newRole.name())) {
            throw new IllegalArgumentException("New role name already exists");
        }
        role.changeName(newRole.name());
        roleRepository.save(role);
    }
}
