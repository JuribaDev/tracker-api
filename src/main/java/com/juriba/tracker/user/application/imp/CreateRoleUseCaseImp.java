package com.juriba.tracker.user.application.imp;

import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.common.domain.EventPublisher;
import com.juriba.tracker.user.application.CreateRoleUseCase;
import com.juriba.tracker.user.domain.Role;
import com.juriba.tracker.user.infrastructure.RoleRepository;
import com.juriba.tracker.user.presentation.dto.RoleRequest;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class CreateRoleUseCaseImp implements CreateRoleUseCase {
        private final RoleRepository roleRepository;
        private final EventPublisher eventPublisher;

    public CreateRoleUseCaseImp(RoleRepository roleRepository, EventPublisher publisher) {
        this.roleRepository = roleRepository;
        this.eventPublisher = publisher;
    }

    @Override
    @Transactional
    public Role execute(RoleRequest roleRequest) {
        String roleName = roleRequest.name().toUpperCase();
        return roleRepository.findByName(roleName)
                .orElseGet(() -> {
                    var role = new Role(roleName);
                    Role savedRole = roleRepository.save(role);
                    role.getDomainEvents().forEach(eventPublisher::publish);
                    role.clearDomainEvents();
                    return savedRole;
                });
    }
}
