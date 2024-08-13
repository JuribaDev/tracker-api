package com.juriba.tracker.user.application;

import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.user.domain.Role;
import com.juriba.tracker.user.domain.User;
import com.juriba.tracker.user.infrastructure.RoleRepository;
import com.juriba.tracker.user.infrastructure.UserRepository;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class AssignRoleToUserUseCase {
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public AssignRoleToUserUseCase(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Transactional
    public void execute(String userId, String roleId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
        Role role = roleRepository.findByName(roleId)
                .orElseThrow(() -> new IllegalArgumentException("Role not found"));
        user.addRole(role);
        userRepository.save(user);
    }
}
