package com.juriba.tracker.expense.application.imp;

import com.juriba.tracker.auth.application.GetAuthenticatedUserUseCase;
import com.juriba.tracker.common.domain.EventPublisher;
import com.juriba.tracker.common.domain.exception.ConflictException;
import com.juriba.tracker.expense.domain.Category;
import com.juriba.tracker.expense.infrastructure.CategoryRepository;
import com.juriba.tracker.expense.presentation.dto.CategoryRequest;
import com.juriba.tracker.expense.presentation.dto.CategoryResponse;
import com.juriba.tracker.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CreateCategoryUseCaseImpEdgeCasesTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;

    @Mock
    private EventPublisher eventPublisher;

    private CreateCategoryUseCaseImp createCategoryUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createCategoryUseCase = new CreateCategoryUseCaseImp(categoryRepository, getAuthenticatedUserUseCase, eventPublisher);
    }

    @Test
    void execute_shouldThrowConflictException_whenCategoryNameAlreadyExists() {
        // Arrange
        User user = new User("John", "john@example.com", "password");
        user.setId("user123");
        CategoryRequest request = new CategoryRequest("Existing Category");

        when(getAuthenticatedUserUseCase.execute()).thenReturn(user);
        when(categoryRepository.existsByNameAndOwner_Id("Existing Category", "user123")).thenReturn(true);

        // Act & Assert
        assertThrows(ConflictException.class, () -> createCategoryUseCase.execute(request));

        verify(categoryRepository).existsByNameAndOwner_Id("Existing Category", "user123");
        verify(categoryRepository, never()).save(any());
        verify(eventPublisher, never()).publish(any());
    }

    @Test
    void execute_shouldCreateCategory_withTrimmedName() {
        // Arrange
        User user = new User("John", "john@example.com", "password");
        user.setId("user123");
        CategoryRequest request = new CategoryRequest("  New Category  ");

        when(getAuthenticatedUserUseCase.execute()).thenReturn(user);
        when(categoryRepository.existsByNameAndOwner_Id("New Category", "user123")).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            Category savedCategory = invocation.getArgument(0);
            savedCategory.setId("category123");
            return savedCategory;
        });

        // Act
        CategoryResponse response = createCategoryUseCase.execute(request);

        // Assert
        assertNotNull(response);
        assertEquals("category123", response.getId());
        assertEquals("New Category", response.getName());

        verify(categoryRepository).existsByNameAndOwner_Id("New Category", "user123");
        verify(categoryRepository).save(argThat(category -> category.getName().equals("New Category")));
        verify(eventPublisher, atLeastOnce()).publish(any());
    }

}