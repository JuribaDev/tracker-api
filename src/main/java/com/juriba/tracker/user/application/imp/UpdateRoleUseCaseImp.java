package com.juriba.tracker.user.application.imp;

import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.common.domain.EventPublisher;
import com.juriba.tracker.user.application.UpdateRoleUseCase;
import com.juriba.tracker.user.domain.Role;
import com.juriba.tracker.user.infrastructure.RoleRepository;
import com.juriba.tracker.user.presentation.dto.RoleRequest;
import com.juriba.tracker.user.presentation.dto.RoleResponse;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class UpdateRoleUseCaseImp implements UpdateRoleUseCase {
    private final RoleRepository roleRepository;
    private final EventPublisher eventPublisher;

    public UpdateRoleUseCaseImp(RoleRepository roleRepository, EventPublisher eventPublisher) {
        this.roleRepository = roleRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public RoleResponse execute(String id, RoleRequest newRole) {
        String name = newRole.name().toUpperCase();
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        if (!role.getName().equals(newRole.name().toUpperCase()) && roleRepository.existsByName(name)) {
            throw new IllegalArgumentException("New role name already exists");
        }
        role.changeName(newRole.name());
        roleRepository.save(role);
        role.getDomainEvents().forEach(eventPublisher::publish);
        role.clearDomainEvents();
        return new RoleResponse(role.getId(), role.getName(),role.getCreatedAt(), role.getModifiedAt());
    }
}
