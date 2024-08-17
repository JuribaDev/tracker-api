package com.juriba.tracker.user.application.imp;

import com.juriba.tracker.common.domain.EventPublisher;
import com.juriba.tracker.user.domain.Role;
import com.juriba.tracker.user.domain.User;
import com.juriba.tracker.user.infrastructure.RoleRepository;
import com.juriba.tracker.user.infrastructure.UserRepository;
import com.juriba.tracker.user.presentation.dto.CreateUserRequest;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateUserUseCaseImpTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private EventPublisher eventPublisher;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    private CreateUserUseCaseImp createUserUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createUserUseCase = new CreateUserUseCaseImp(userRepository, eventPublisher, roleRepository, passwordEncoder);
    }

    @Test
    void execute_shouldCreateUser_whenValidRequest() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest("user@example.com", "password", "John Doe");
        when(userRepository.existsByEmail("user@example.com")).thenReturn(false);
        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");

        Role userRole = new Role("USER");
        when(roleRepository.findByName("USER")).thenReturn(Optional.of(userRole));

        User savedUser = new User("John Doe", "user@example.com", "encodedPassword");
        savedUser.addRole(userRole);
        when(userRepository.save(any(User.class))).thenReturn(savedUser);

        // Act
        User result = createUserUseCase.execute(request);

        // Assert
        assertNotNull(result, "Result should not be null");
        assertEquals("user@example.com", result.getEmail());
        assertEquals("encodedPassword", result.getPassword());

        // Verify
        verify(userRepository).existsByEmail("user@example.com");
        verify(passwordEncoder).encode("password");
        verify(roleRepository).findByName("USER");
        verify(userRepository).save(any(User.class));
        verify(eventPublisher, atLeastOnce()).publish(any());
    }

    @Test
    void execute_shouldThrowException_whenUserAlreadyExists() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest("existing@example.com", "password", "John Doe");
        when(userRepository.existsByEmail("existing@example.com")).thenReturn(true);

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> createUserUseCase.execute(request));

        verify(userRepository).existsByEmail("existing@example.com");
        verifyNoMoreInteractions(userRepository, passwordEncoder, roleRepository, eventPublisher);
    }

    @Test
    void execute_shouldThrowException_whenDefaultRoleNotFound() {
        // Arrange
        CreateUserRequest request = new CreateUserRequest("user@example.com", "password", "John Doe");
        when(userRepository.existsByEmail("user@example.com")).thenReturn(false);
        when(roleRepository.findByName("USER")).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalStateException.class, () -> createUserUseCase.execute(request));

        verify(userRepository).existsByEmail("user@example.com");
        verify(roleRepository).findByName("USER");
        verifyNoMoreInteractions(userRepository, passwordEncoder, eventPublisher);
    }
}