package com.juriba.tracker.user.application.imp;

import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.common.domain.EventPublisher;
import com.juriba.tracker.user.application.AssignRoleToUserUseCase;
import com.juriba.tracker.user.domain.Role;
import com.juriba.tracker.user.domain.User;
import com.juriba.tracker.user.infrastructure.RoleRepository;
import com.juriba.tracker.user.infrastructure.UserRepository;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class AssignRoleToUserUseCaseImp implements AssignRoleToUserUseCase {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final EventPublisher eventPublisher;

    public AssignRoleToUserUseCaseImp(UserRepository userRepository, RoleRepository roleRepository, EventPublisher eventPublisher) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.eventPublisher = eventPublisher;
    }

    @Override
    @Transactional
    public void execute(String userId, String roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Role role = roleRepository.findByName(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        user.addRole(role);
        userRepository.save(user);
        user.getDomainEvents().forEach(eventPublisher::publish);
        user.clearDomainEvents();
    }
}
