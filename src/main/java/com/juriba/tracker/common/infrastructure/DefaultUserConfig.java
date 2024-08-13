package com.juriba.tracker.common.infrastructure;

import com.juriba.tracker.user.application.CreateRoleUseCase;
import com.juriba.tracker.user.domain.Role;
import com.juriba.tracker.user.domain.User;
import com.juriba.tracker.user.infrastructure.RoleRepository;
import com.juriba.tracker.user.infrastructure.UserRepository;
import com.juriba.tracker.user.presentation.dto.RoleRequest;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DefaultUserConfig {

    @Bean
    public CommandLineRunner initializeDefaultUsers(
            UserRepository userRepository,
            CreateRoleUseCase roleRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            roleRepository.execute(new RoleRequest("ADMIN"));
            roleRepository.execute(new RoleRequest("USER"));


            if (userRepository.findByEmail("admin@tracker.com").isEmpty()) {
                User adminUser = new User("admin", "admin@tracker.com", passwordEncoder.encode("adminPassword"));
                adminUser.addRole(new Role("ADMIN"));
                adminUser.addRole(new Role("USER"));
                userRepository.save(adminUser);
            }

            // Create regular user if it doesn't exist
            if (userRepository.findByEmail("user@tracker.com").isEmpty()) {
                User regularUser = new User("user", "user@tracker.com", passwordEncoder.encode("userPassword"));
                regularUser.addRole(new Role("USER"));
                userRepository.save(regularUser);
            }
        };
    }
}