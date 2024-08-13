package com.juriba.tracker.user.application;

import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.user.domain.Role;
import com.juriba.tracker.user.infrastructure.RoleRepository;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class DeleteRoleUseCase {
    private final RoleRepository roleRepository;

    public DeleteRoleUseCase(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void execute(String id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        roleRepository.delete(role);
    }
}