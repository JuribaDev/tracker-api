package com.juriba.tracker.user.application.imp;

import com.juriba.tracker.common.domain.EventPublisher;
import com.juriba.tracker.common.presentation.dto.CommonSuccessResponse;
import com.juriba.tracker.user.domain.Role;
import com.juriba.tracker.user.domain.User;
import com.juriba.tracker.user.infrastructure.RoleRepository;
import com.juriba.tracker.user.infrastructure.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class AssignRoleToUserUseCaseImpTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private EventPublisher eventPublisher;

    private AssignRoleToUserUseCaseImp assignRoleToUserUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        assignRoleToUserUseCase = new AssignRoleToUserUseCaseImp(userRepository, roleRepository, eventPublisher);
    }

    @Test
    void execute_shouldAssignRoleToUser_whenValidUserAndRole() {
        // Arrange
        String userId = "user123";
        String roleId = "ADMIN";
        User user = new User("John", "john@example.com", "password");
        user.setId(userId);
        Role role = new Role(roleId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByName(roleId)).thenReturn(Optional.of(role));
        when(userRepository.save(any(User.class))).thenReturn(user);

        // Act
        CommonSuccessResponse response = assignRoleToUserUseCase.execute(userId, roleId);

        // Assert
        assertNotNull(response);
        assertEquals("Role: ADMIN assigned to user: john@example.com", response.message());

        verify(userRepository).findById(userId);
        verify(roleRepository).findByName(roleId);
        verify(userRepository).save(user);
        verify(eventPublisher, atLeastOnce()).publish(any());
    }

    @Test
    void execute_shouldThrowException_whenUserNotFound() {
        // Arrange
        String userId = "nonexistent123";
        String roleId = "ADMIN";

        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> assignRoleToUserUseCase.execute(userId, roleId));

        verify(userRepository).findById(userId);
        verify(roleRepository, never()).findByName(any());
        verify(userRepository, never()).save(any());
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    void execute_shouldThrowException_whenRoleNotFound() {
        // Arrange
        String userId = "user123";
        String roleId = "NONEXISTENT_ROLE";
        User user = new User("John", "john@example.com", "password");
        user.setId(userId);

        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(roleRepository.findByName(roleId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> assignRoleToUserUseCase.execute(userId, roleId));

        verify(userRepository).findById(userId);
        verify(roleRepository).findByName(roleId);
        verify(userRepository, never()).save(any());
        verify(eventPublisher, never()).publish(any());
    }
}