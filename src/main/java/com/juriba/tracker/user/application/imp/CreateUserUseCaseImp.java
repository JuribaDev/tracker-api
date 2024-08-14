package com.juriba.tracker.user.application.imp;

import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.common.domain.EventPublisher;
import com.juriba.tracker.user.application.CreateUserUseCase;
import com.juriba.tracker.user.domain.Role;
import com.juriba.tracker.user.domain.User;
import com.juriba.tracker.user.infrastructure.RoleRepository;
import com.juriba.tracker.user.infrastructure.UserRepository;
import com.juriba.tracker.user.presentation.dto.CreateUserRequest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.transaction.annotation.Transactional;

@UseCase
public class CreateUserUseCaseImp implements CreateUserUseCase {
    private final UserRepository userRepository;
    private final EventPublisher eventPublisher;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public CreateUserUseCaseImp(UserRepository userRepository, EventPublisher eventPublisher, RoleRepository roleRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.eventPublisher = eventPublisher;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    @Transactional
    public User execute(CreateUserRequest createUserRequest) {
        if(userRepository.existsByEmail(createUserRequest.email())) {
            throw new IllegalArgumentException("User already exists");
        }

        User user = new User(
                createUserRequest.name(),
                createUserRequest.email(),
                passwordEncoder.encode(createUserRequest.password())
        );

        Role userRole = roleRepository.findByName("USER")
                .orElseThrow(() -> new IllegalStateException("Default USER role not found"));

        user.addRole(userRole);

        User savedUser = userRepository.save(user);

        savedUser.getDomainEvents().forEach(eventPublisher::publish);
        savedUser.clearDomainEvents();

        return savedUser;
    }
}
