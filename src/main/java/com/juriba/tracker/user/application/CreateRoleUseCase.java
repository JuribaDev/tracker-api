package com.juriba.tracker.user.application;

import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.common.domain.EventPublisher;
import com.juriba.tracker.user.domain.Role;
import com.juriba.tracker.user.infrastructure.RoleRepository;
import com.juriba.tracker.user.presentation.dto.RoleRequest;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class CreateRoleUseCase {
        private final RoleRepository roleRepository;
        private final EventPublisher eventPublisher;

    public CreateRoleUseCase(RoleRepository roleRepository, EventPublisher publisher) {
        this.roleRepository = roleRepository;
        this.eventPublisher = publisher;
    }


    @Transactional
    public void execute(RoleRequest roleRequest) {
            if (roleRepository.existsByName(roleRequest.name())) {
                throw new IllegalArgumentException("Role already exists");
            }
            var role = new Role(roleRequest.name().toUpperCase());
            roleRepository.save(role);
            role.getDomainEvents().forEach(eventPublisher::publish);
            role.clearDomainEvents();
        }
}
