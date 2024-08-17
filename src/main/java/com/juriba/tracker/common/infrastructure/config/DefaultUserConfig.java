package com.juriba.tracker.common.infrastructure.config;

import com.juriba.tracker.user.application.CreateRoleUseCase;
import com.juriba.tracker.user.application.CreateUserUseCase;
import com.juriba.tracker.user.domain.Role;
import com.juriba.tracker.user.domain.User;
import com.juriba.tracker.user.infrastructure.RoleRepository;
import com.juriba.tracker.user.infrastructure.UserRepository;
import com.juriba.tracker.user.presentation.dto.CreateUserRequest;
import com.juriba.tracker.user.presentation.dto.RoleRequest;
import jakarta.transaction.Transactional;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

@Configuration
@Profile("dev")
public class DefaultUserConfig {

    @Bean
    @Transactional
    public CommandLineRunner initializeDefaultUsers(
            UserRepository userRepository,
            RoleRepository roleRepository,
            CreateUserUseCase createUserUseCase) {

        return args -> {

            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role("ADMIN")));

            Role userRole = roleRepository.findByName("USER")
                    .orElseGet(() -> roleRepository.save(new Role("USER")));

            if (userRepository.findByEmail("admin@tracker.com").isEmpty()) {
                CreateUserRequest adminRequest = new CreateUserRequest("admin@tracker.com", "adminPassword", "Admin");
                User adminUser = createUserUseCase.execute(adminRequest);
                adminUser.addRole(adminRole);
                userRepository.save(adminUser);
            }

            if (userRepository.findByEmail("user@tracker.com").isEmpty()) {
                CreateUserRequest userRequest = new CreateUserRequest("user@tracker.com", "userPassword", "User");
                User userUser = createUserUseCase.execute(userRequest);
                userUser.addRole(userRole);
                userRepository.save(userUser);
            }
        };
    }
}
