package com.juriba.tracker.common.infrastructure;

import com.juriba.tracker.user.domain.Role;
import com.juriba.tracker.user.domain.User;
import com.juriba.tracker.user.infrastructure.RoleRepository;
import com.juriba.tracker.user.infrastructure.UserRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;

@Configuration
public class DefaultUserConfig {

    @Bean
    public CommandLineRunner initializeDefaultUsers(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PasswordEncoder passwordEncoder) {

        return args -> {
            Role adminRole = roleRepository.findByName("ADMIN")
                    .orElseGet(() -> roleRepository.save(new Role("ADMIN")));
            Role userRole = roleRepository.findByName("USER")
                    .orElseGet(() -> roleRepository.save(new Role("USER")));

            if (userRepository.findByEmail("admin@tracker.com").isEmpty()) {
                User adminUser = new User("admin", "admin@tracker.com", passwordEncoder.encode("adminPassword"));
                adminUser.addRole(adminRole);
                adminUser.addRole(userRole);
                userRepository.save(adminUser);
            }

            // Create regular user if it doesn't exist
            if (userRepository.findByEmail("user@tracker.com").isEmpty()) {
                User regularUser = new User("user", "user@tracker.com", passwordEncoder.encode("userPassword"));
                regularUser.addRole(userRole);
                userRepository.save(regularUser);
            }
        };
    }
}