package com.juriba.tracker.expense.presentation.mapper;

import com.juriba.tracker.expense.domain.Category;
import com.juriba.tracker.expense.domain.Expense;
import com.juriba.tracker.expense.presentation.dto.ExpenseResponse;
import com.juriba.tracker.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.hateoas.Link;

import java.math.BigDecimal;
import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

class ExpenseMapperTest {

    @Test
    void toExpenseResponse_shouldMapAllFields() {
        // Arrange
        User user = new User("John", "john@example.com", "password");
        user.setId("user123");
        Category category = new Category("Food", user, false);
        category.setId("category123");
        Expense expense = new Expense("Lunch", BigDecimal.valueOf(10.50), category, user);
        expense.setId("expense123");
        Instant now = Instant.now();
        expense.setCreatedAt(now);

        // Act
        ExpenseResponse response = ExpenseMapper.toExpenseResponse(expense);

        // Assert
        assertNotNull(response);
        assertEquals("expense123", response.getId());
        assertEquals("Lunch", response.getDescription());
        assertEquals(BigDecimal.valueOf(10.50), response.getAmount());
        assertEquals(now, response.getCreatedAt());
        assertNotNull(response.getCategory());
        assertEquals("category123", response.getCategory().getId());
        assertEquals("Food", response.getCategory().getName());
        assertFalse(response.getCategory().isDefault());

        // Check if HATEOAS link is present
        assertTrue(response.getLinks().hasSize(1));
        Link selfLink = response.getLink("self").orElse(null);
        assertNotNull(selfLink);
        assertTrue(selfLink.getHref().contains("/api/v1/expenses/expense123"));
    }


}