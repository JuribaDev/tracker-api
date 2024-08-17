package com.juriba.tracker.expense.application.imp;

import com.juriba.tracker.auth.application.GetAuthenticatedUserUseCase;
import com.juriba.tracker.expense.domain.Category;
import com.juriba.tracker.expense.infrastructure.CategoryRepository;
import com.juriba.tracker.expense.presentation.dto.CategoryResponse;
import com.juriba.tracker.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.*;

class GetListCategoriesUseCaseImplTest {

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;

    private GetListCategoriesUseCaseImpl getListCategoriesUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        getListCategoriesUseCase = new GetListCategoriesUseCaseImpl(categoryRepository, getAuthenticatedUserUseCase);
    }

    @Test
    void execute_shouldReturnPagedCategories_forAuthenticatedUser() {
        // Arrange
        User user = new User("John", "john@example.com", "password");
        user.setId("user123");
        when(getAuthenticatedUserUseCase.execute()).thenReturn(user);

        Category category1 = new Category("Category 1", user, false);
        category1.setId("cat1");
        Category category2 = new Category("Category 2", user, false);
        category2.setId("cat2");
        List<Category> categories = Arrays.asList(category1, category2);
        Page<Category> categoryPage = new PageImpl<>(categories);

        when(categoryRepository.findAllByOwner_Id(eq("user123"), any(PageRequest.class))).thenReturn(categoryPage);

        // Act
        Page<CategoryResponse> result = getListCategoriesUseCase.execute(0, 10, "name", "ASC");

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals("Category 1", result.getContent().get(0).getName());
        assertEquals("Category 2", result.getContent().get(1).getName());

        verify(getAuthenticatedUserUseCase).execute();
        verify(categoryRepository).findAllByOwner_Id(eq("user123"), any(PageRequest.class));
    }

    @Test
    void execute_shouldUseSortingParameters() {
        // Arrange
        User user = new User("John", "john@example.com", "password");
        user.setId("user123");
        when(getAuthenticatedUserUseCase.execute()).thenReturn(user);

        when(categoryRepository.findAllByOwner_Id(eq("user123"), any(PageRequest.class))).thenReturn(Page.empty());

        // Act
        getListCategoriesUseCase.execute(0, 10, "createdAt", "DESC");

        // Assert
        verify(categoryRepository).findAllByOwner_Id(eq("user123"),
                argThat(pageRequest ->
                        pageRequest.getPageNumber() == 0 &&
                                pageRequest.getPageSize() == 10 &&
                                pageRequest.getSort().equals(Sort.by(Sort.Direction.DESC, "createdAt"))
                )
        );
    }

    @Test
    void execute_shouldHandleEmptyResult() {
        // Arrange
        User user = new User("John", "john@example.com", "password");
        user.setId("user123");
        when(getAuthenticatedUserUseCase.execute()).thenReturn(user);

        when(categoryRepository.findAllByOwner_Id(eq("user123"), any(PageRequest.class))).thenReturn(Page.empty());

        // Act
        Page<CategoryResponse> result = getListCategoriesUseCase.execute(0, 10, "name", "ASC");

        // Assert
        assertNotNull(result);
        assertEquals(0, result.getTotalElements());
        assertTrue(result.getContent().isEmpty());

        verify(getAuthenticatedUserUseCase).execute();
        verify(categoryRepository).findAllByOwner_Id(eq("user123"), any(PageRequest.class));
    }
}