package com.juriba.tracker.user.application.imp;

import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.common.domain.EventPublisher;
import com.juriba.tracker.user.application.DeleteRoleUseCase;
import com.juriba.tracker.user.domain.Role;
import com.juriba.tracker.user.infrastructure.RoleRepository;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class DeleteRoleUseCaseImp implements DeleteRoleUseCase {
    private final RoleRepository roleRepository;
    private final EventPublisher eventPublisher;

    public DeleteRoleUseCaseImp(RoleRepository roleRepository, EventPublisher eventPublisher) {
        this.roleRepository = roleRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public void execute(String id) {
        Role role = roleRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        roleRepository.delete(role);
        role.getDomainEvents().forEach(eventPublisher::publish);
        role.clearDomainEvents();
    }
}