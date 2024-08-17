package com.juriba.tracker.user.application.imp;

import com.juriba.tracker.common.domain.EventPublisher;
import com.juriba.tracker.user.domain.Role;
import com.juriba.tracker.user.infrastructure.RoleRepository;
import com.juriba.tracker.user.presentation.dto.RoleRequest;
import com.juriba.tracker.user.presentation.dto.RoleResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.time.Instant;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateRoleUseCaseImpTest {

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private EventPublisher eventPublisher;

    private CreateRoleUseCaseImp createRoleUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createRoleUseCase = new CreateRoleUseCaseImp(roleRepository, eventPublisher);
    }

    @Test
    void execute_shouldCreateNewRole_whenRoleDoesNotExist() {
        // Arrange
        RoleRequest request = new RoleRequest("NEW_ROLE");
        Role newRole = new Role("NEW_ROLE");
        newRole.setId("role123");
        Instant now = Instant.now();
        newRole.setCreatedAt(now);
        newRole.setModifiedAt(now);

        when(roleRepository.findByName("NEW_ROLE")).thenReturn(Optional.empty());
        when(roleRepository.save(any(Role.class))).thenReturn(newRole);

        // Act
        RoleResponse response = createRoleUseCase.execute(request);

        // Assert
        assertNotNull(response);
        assertEquals("role123", response.id());
        assertEquals("NEW_ROLE", response.name());
        assertEquals(now, response.createdAt());
        assertEquals(now, response.updatedAt());

        verify(roleRepository).findByName("NEW_ROLE");
        verify(roleRepository).save(any(Role.class));
        verify(eventPublisher, atLeastOnce()).publish(any());
    }

    @Test
    void execute_shouldReturnExistingRole_whenRoleAlreadyExists() {
        // Arrange
        RoleRequest request = new RoleRequest("EXISTING_ROLE");
        Role existingRole = new Role("EXISTING_ROLE");
        existingRole.setId("role456");
        Instant now = Instant.now();
        existingRole.setCreatedAt(now);
        existingRole.setModifiedAt(now);

        when(roleRepository.findByName("EXISTING_ROLE")).thenReturn(Optional.of(existingRole));

        // Act
        RoleResponse response = createRoleUseCase.execute(request);

        // Assert
        assertNotNull(response);
        assertEquals("role456", response.id());
        assertEquals("EXISTING_ROLE", response.name());
        assertEquals(now, response.createdAt());
        assertEquals(now, response.updatedAt());

        verify(roleRepository).findByName("EXISTING_ROLE");
        verify(roleRepository, never()).save(any(Role.class));
    }

    @Test
    void execute_shouldConvertRoleNameToUpperCase() {
        // Arrange
        RoleRequest request = new RoleRequest("new_role");
        Role newRole = new Role("NEW_ROLE");
        newRole.setId("role789");

        when(roleRepository.findByName("NEW_ROLE")).thenReturn(Optional.empty());
        when(roleRepository.save(any(Role.class))).thenReturn(newRole);

        // Act
        RoleResponse response = createRoleUseCase.execute(request);

        // Assert
        assertNotNull(response);
        assertEquals("NEW_ROLE", response.name());

        verify(roleRepository).findByName("NEW_ROLE");
        verify(roleRepository).save(any(Role.class));
    }
}