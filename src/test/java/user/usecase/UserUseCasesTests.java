package user.usecase;


import com.juriba.tracker.common.domain.EventPublisher;
import com.juriba.tracker.common.domain.exception.ConflictException;
import com.juriba.tracker.user.domain.Role;
import com.juriba.tracker.user.domain.User;
import com.juriba.tracker.user.infrastructure.RoleRepository;
import com.juriba.tracker.user.infrastructure.UserRepository;
import com.juriba.tracker.user.presentation.dto.CreateUserRequest;
import com.juriba.tracker.user.presentation.dto.RoleRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;
import com.juriba.tracker.user.application.imp.CreateRoleUseCaseImp;
import com.juriba.tracker.user.application.imp.CreateUserUseCaseImp;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UserUseCasesTests {

    @Mock private UserRepository userRepository;
    @Mock private RoleRepository roleRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private EventPublisher eventPublisher;

    private CreateUserUseCaseImp createUserUseCase;
    private CreateRoleUseCaseImp createRoleUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createUserUseCase = new CreateUserUseCaseImp(userRepository,eventPublisher, roleRepository, passwordEncoder);
        createRoleUseCase = new CreateRoleUseCaseImp(roleRepository,eventPublisher);
    }

    @Test
    void createUserUseCase_SuccessfulCreation() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest("test@example.com", "password", "Test User");
        when(userRepository.existsByEmail("test@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encoded_password");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(new Role("USER")));
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User user = invocation.getArgument(0);
            user.setId("user_id");
            return user;
        });

        // Act
        User createdUser = createUserUseCase.execute(request);

        // Assert
        assertNotNull(createdUser);
        assertEquals("user_id", createdUser.getId());
        assertEquals("test@example.com", createdUser.getEmail());
        assertEquals("encoded_password", createdUser.getPassword());
        assertTrue(createdUser.getRoles().stream().anyMatch(role -> role.getName().equals("USER")));
    }



    @Test
    void createRoleUseCase_SuccessfulCreation() {
        // Arrange
        RoleRequest request = new RoleRequest("ADMIN");
        when(roleRepository.existsByName("ADMIN")).thenReturn(false);
        when(roleRepository.save(any(Role.class))).thenAnswer(invocation -> {
            Role role = invocation.getArgument(0);
            role.setId("role_id");
            return role;
        });

        // Act
        Role createdRole = createRoleUseCase.execute(request);

        // Assert
        assertNotNull(createdRole);
        assertEquals("role_id", createdRole.getId());
        assertEquals("ADMIN", createdRole.getName());
    }


}