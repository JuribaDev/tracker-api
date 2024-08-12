package com.juriba.tracker.user.application;

import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.user.domain.Role;
import com.juriba.tracker.user.infrastructure.RoleRepository;
import com.juriba.tracker.user.presentation.dto.RoleRequest;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class CreateRoleUseCase {
        private final RoleRepository roleRepository;

    public CreateRoleUseCase(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }


    @Transactional
    public void execute(RoleRequest role) {
            if (roleRepository.existsByName(role.name())) {
                throw new IllegalArgumentException("Role already exists");
            }
            roleRepository.save(new Role(role.name()));
        }
}
