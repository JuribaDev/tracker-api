package expense.usecase;

import com.juriba.tracker.auth.application.GetAuthenticatedUserUseCase;
import com.juriba.tracker.common.domain.exception.ResourceNotFoundException;
import com.juriba.tracker.expense.application.imp.CreateExpenseUseCaseImp;
import com.juriba.tracker.expense.application.imp.GetExpenseUseCaseImp;
import com.juriba.tracker.expense.application.imp.GetListExpensesUseCaseImp;
import com.juriba.tracker.expense.application.imp.UpdateExpenseUseCaseImp;
import com.juriba.tracker.expense.domain.Category;
import com.juriba.tracker.expense.domain.Expense;
import com.juriba.tracker.expense.infrastructure.CategoryRepository;
import com.juriba.tracker.expense.infrastructure.ExpenseRepository;
import com.juriba.tracker.expense.presentation.dto.*;
import com.juriba.tracker.user.domain.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.time.Instant;
import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class ExpenseUseCasesTests {

    @Mock private ExpenseRepository expenseRepository;
    @Mock private CategoryRepository categoryRepository;
    @Mock private GetAuthenticatedUserUseCase getAuthenticatedUserUseCase;

    private CreateExpenseUseCaseImp createExpenseUseCase;
    private GetExpenseUseCaseImp getExpenseUseCase;
    private GetListExpensesUseCaseImp getListExpensesUseCase;
    private UpdateExpenseUseCaseImp updateExpenseUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        createExpenseUseCase = new CreateExpenseUseCaseImp(expenseRepository, categoryRepository, getAuthenticatedUserUseCase);
        getExpenseUseCase = new GetExpenseUseCaseImp(expenseRepository);
        getListExpensesUseCase = new GetListExpensesUseCaseImp(expenseRepository);
        updateExpenseUseCase = new UpdateExpenseUseCaseImp(expenseRepository, categoryRepository);
    }

    @Test
    void createExpenseUseCase_SuccessfulCreation() {
        // Arrange
        ExpenseRequest request = new ExpenseRequest("Test Expense", new BigDecimal("100.00"), "category_id");
        User user = createTestUser();
        Category category = new Category("Test Category", user, false);
        category.setId("category_id");
        Expense expense = new Expense("Test Expense", new BigDecimal("100.00"), category);
        expense.setId("expense_id");

        when(getAuthenticatedUserUseCase.execute()).thenReturn(user);
        when(categoryRepository.findById("category_id")).thenReturn(Optional.of(category));
        when(expenseRepository.save(any(Expense.class))).thenReturn(expense);

        // Act
        ExpenseResponse response = createExpenseUseCase.execute(request);

        // Assert
        assertNotNull(response);
        assertEquals("expense_id", response.getId());
        assertEquals("Test Expense", response.getDescription());
        assertEquals(new BigDecimal("100.00"), response.getAmount());
    }

    @Test
    void getExpenseUseCase_ExistingExpense() {
        // Arrange
        String expenseId = "expense_id";
        User user = createTestUser();
        Category category = new Category("Test Category", user, false);
        Expense expense = new Expense("Test Expense", new BigDecimal("100.00"), category);
        expense.setId(expenseId);
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(expense));

        // Act
        ExpenseResponse response = getExpenseUseCase.execute(expenseId);

        // Assert
        assertNotNull(response);
        assertEquals(expenseId, response.getId());
        assertEquals("Test Expense", response.getDescription());
        assertEquals(new BigDecimal("100.00"), response.getAmount());
    }

    @Test
    void getExpenseUseCase_NonExistingExpense() {
        // Arrange
        String expenseId = "non_existing_id";
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(ResourceNotFoundException.class, () -> getExpenseUseCase.execute(expenseId));
    }

    @Test
    void getListExpensesUseCase_SuccessfulRetrieval() {
        // Arrange
        Instant startDate = Instant.now().minusSeconds(3600*24*7);
        Instant endDate = Instant.now();
        User user = createTestUser();
        Category category = new Category("Test Category", user, false);
        Expense expense = new Expense("Test Expense", new BigDecimal("100.00"), category);
        expense.setId("expense_id");
        Page<Expense> expensePage = new PageImpl<>(Collections.singletonList(expense));
        when(expenseRepository.findByCreatedAtBetween(eq(startDate), eq(endDate), any(Pageable.class))).thenReturn(expensePage);

        // Act
        Page<ExpenseResponse> response = getListExpensesUseCase.execute(startDate, endDate, 0, 10, "createdAt", "DESC");

        // Assert
        assertNotNull(response);
        assertEquals(1, response.getTotalElements());
        assertEquals("Test Expense", response.getContent().get(0).getDescription());
    }

    @Test
    void updateExpenseUseCase_SuccessfulUpdate() {
        // Arrange
        String expenseId = "expense_id";
        UpdateExpenseRequest request = new UpdateExpenseRequest("Updated Expense", new BigDecimal("200.00"));
        User user = createTestUser();
        Category category = new Category("Test Category", user, false);
        Expense existingExpense = new Expense("Test Expense", new BigDecimal("100.00"), category);
        existingExpense.setId(expenseId);
        when(expenseRepository.findById(expenseId)).thenReturn(Optional.of(existingExpense));
        when(categoryRepository.findById(existingExpense.getCategory().getId())).thenReturn(Optional.of(existingExpense.getCategory()));
        when(expenseRepository.save(any(Expense.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Act
        ExpenseResponse response = updateExpenseUseCase.execute(expenseId, request);

        // Assert
        assertNotNull(response);
        assertEquals(expenseId, response.getId());
        assertEquals("Updated Expense", response.getDescription());
        assertEquals(new BigDecimal("200.00"), response.getAmount());
    }

    // Helper method to create a test User
    private User createTestUser() {
        User user = new User("Test User", "test@example.com", "password");
        user.setId("user_id");
        return user;
    }
}
