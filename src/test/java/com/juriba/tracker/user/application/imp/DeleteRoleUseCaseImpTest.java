package com.juriba.tracker.user.application.imp;

import com.juriba.tracker.common.domain.EventPublisher;
import com.juriba.tracker.common.presentation.dto.CommonSuccessResponse;
import com.juriba.tracker.user.domain.Role;
import com.juriba.tracker.user.infrastructure.RoleRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class DeleteRoleUseCaseImpTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private EventPublisher eventPublisher;

    private DeleteRoleUseCaseImp deleteRoleUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        deleteRoleUseCase = new DeleteRoleUseCaseImp(roleRepository, eventPublisher);
    }

    @Test
    void execute_shouldDeleteRole_whenValidRoleId() {
        // Arrange
        String roleId = "role123";
        Role role = new Role("CUSTOM_ROLE");
        role.setId(roleId);

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(role));

        // Act
        CommonSuccessResponse response = deleteRoleUseCase.execute(roleId);

        // Assert
        assertNotNull(response);
        assertEquals("Role: CUSTOM_ROLE deleted", response.message());

        verify(roleRepository).findById(roleId);
        verify(roleRepository).delete(role);
        verify(eventPublisher, atLeastOnce()).publish(any());
    }

    @Test
    void execute_shouldThrowException_whenRoleNotFound() {
        // Arrange
        String roleId = "nonexistent123";
        when(roleRepository.findById(roleId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> deleteRoleUseCase.execute(roleId));

        verify(roleRepository).findById(roleId);
        verify(roleRepository, never()).delete(any());
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    void execute_shouldThrowException_whenDeletingUserRole() {
        // Arrange
        String roleId = "user123";
        Role userRole = new Role("USER");
        userRole.setId(roleId);

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(userRole));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> deleteRoleUseCase.execute(roleId));

        verify(roleRepository).findById(roleId);
        verify(roleRepository, never()).delete(any());
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    void execute_shouldThrowException_whenDeletingAdminRole() {
        // Arrange
        String roleId = "admin123";
        Role adminRole = new Role("ADMIN");
        adminRole.setId(roleId);

        when(roleRepository.findById(roleId)).thenReturn(Optional.of(adminRole));

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> deleteRoleUseCase.execute(roleId));

        verify(roleRepository).findById(roleId);
        verify(roleRepository, never()).delete(any());
        verify(eventPublisher, never()).publish(any());
    }
}