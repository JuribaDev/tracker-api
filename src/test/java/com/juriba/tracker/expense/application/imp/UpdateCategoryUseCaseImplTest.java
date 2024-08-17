package com.juriba.tracker.expense.application.imp;

import com.juriba.tracker.auth.application.GetAuthenticatedUserUseCase;
import com.juriba.tracker.common.domain.EventPublisher;
import com.juriba.tracker.common.domain.exception.ResourceNotFoundException;
import com.juriba.tracker.expense.domain.Category;
import com.juriba.tracker.expense.infrastructure.CategoryRepository;
import com.juriba.tracker.expense.presentation.dto.CategoryRequest;
import com.juriba.tracker.expense.presentation.dto.CategoryResponse;
import com.juriba.tracker.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class UpdateCategoryUseCaseImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;

    @Mock
    private EventPublisher eventPublisher;

    private UpdateCategoryUseCaseImpl updateCategoryUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        updateCategoryUseCase = new UpdateCategoryUseCaseImpl(categoryRepository, getAuthenticatedUserUseCase, eventPublisher);
    }

    @Test
    void execute_shouldUpdateCategory_whenValidRequestAndOwner() {
        // Arrange
        String categoryId = "category123";
        User user = new User("John", "john@example.com", "password");
        user.setId("user123");
        Category category = new Category("Old Name", user, false);
        category.setId(categoryId);

        CategoryRequest request = new CategoryRequest("New Name");

        when(getAuthenticatedUserUseCase.execute()).thenReturn(user);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));
        when(categoryRepository.save(any(Category.class))).thenReturn(category);

        // Act
        CategoryResponse response = updateCategoryUseCase.execute(categoryId, request);

        // Assert
        assertNotNull(response);
        assertEquals(categoryId, response.getId());
        assertEquals("New Name", response.getName());
        assertFalse(response.isDefault());

        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository).save(category);
        verify(eventPublisher, atLeastOnce()).publish(any());
    }

    @Test
    void execute_shouldThrowResourceNotFoundException_whenCategoryNotFound() {
        // Arrange
        String categoryId = "nonexistent123";
        User user = new User("John", "john@example.com", "password");
        user.setId("user123");
        CategoryRequest request = new CategoryRequest("New Name");

        when(getAuthenticatedUserUseCase.execute()).thenReturn(user);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> updateCategoryUseCase.execute(categoryId, request));

        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository, never()).save(any());
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    void execute_shouldThrowResourceNotFoundException_whenUserIsNotOwner() {
        // Arrange
        String categoryId = "category123";
        User user = new User("John", "john@example.com", "password");
        user.setId("user123");
        User otherUser = new User("Jane", "jane@example.com", "password");
        otherUser.setId("user456");
        Category category = new Category("Old Name", otherUser, false);
        category.setId(categoryId);

        CategoryRequest request = new CategoryRequest("New Name");

        when(getAuthenticatedUserUseCase.execute()).thenReturn(user);
        when(categoryRepository.findById(categoryId)).thenReturn(Optional.of(category));

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> updateCategoryUseCase.execute(categoryId, request));

        verify(categoryRepository).findById(categoryId);
        verify(categoryRepository, never()).save(any());
        verify(eventPublisher, never()).publish(any());
    }
}