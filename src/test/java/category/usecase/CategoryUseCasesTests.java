package category.usecase;


import com.juriba.tracker.auth.application.GetAuthenticatedUserUseCase;
import com.juriba.tracker.common.domain.exception.ConflictException;
import com.juriba.tracker.expense.application.imp.*;
import com.juriba.tracker.expense.domain.Category;
import com.juriba.tracker.expense.infrastructure.CategoryRepository;
import com.juriba.tracker.expense.presentation.dto.CategoryRequest;
import com.juriba.tracker.expense.presentation.dto.CategoryResponse;
import com.juriba.tracker.user.domain.User;
import com.juriba.tracker.user.infrastructure.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class CategoryUseCasesTests {

    @Mock private CategoryRepository categoryRepository;
    @Mock private UserRepository userRepository;
    @Mock private GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;

    private CreateCategoryUseCaseImp createCategoryUseCase;


    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createCategoryUseCase = new CreateCategoryUseCaseImp(categoryRepository, userRepository, getAuthenticatedUserUseCase);
    }

    @Test
    void createCategoryUseCase_SuccessfulCreation() {
        // Arrange
        CategoryRequest request = new CategoryRequest("Test Category");
        User user = createTestUser();
        when(getAuthenticatedUserUseCase.execute()).thenReturn(user);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(categoryRepository.existsByNameAndOwner("Test Category", user)).thenReturn(false);
        when(categoryRepository.save(any(Category.class))).thenAnswer(invocation -> {
            Category category = invocation.getArgument(0);
            category.setId("category_id");
            return category;
        });

        // Act
        CategoryResponse response = createCategoryUseCase.execute(request);

        // Assert
        assertNotNull(response);
        assertEquals("category_id", response.id());
        assertEquals("Test Category", response.name());
        assertFalse(response.isDefault());
    }

    @Test
    void createCategoryUseCase_CategoryAlreadyExists() {
        // Arrange
        CategoryRequest request = new CategoryRequest("Existing Category");
        User user = createTestUser();
        when(getAuthenticatedUserUseCase.execute()).thenReturn(user);
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(user));
        when(categoryRepository.existsByNameAndOwner("Existing Category", user)).thenReturn(true);

        // Act & Assert
        assertThrows(ConflictException.class, () -> createCategoryUseCase.execute(request));
    }






    // Helper method to create a test User
    private User createTestUser() {
        User user = new User("Test User", "test@example.com", "password");
        user.setId("user_id");
        return user;
    }
}
