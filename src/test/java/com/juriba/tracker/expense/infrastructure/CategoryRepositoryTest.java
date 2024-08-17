package com.juriba.tracker.expense.infrastructure;

import com.juriba.tracker.expense.domain.Category;
import com.juriba.tracker.user.domain.User;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;

import java.time.Instant;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class CategoryRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CategoryRepository categoryRepository;

    private User createAndPersistUser(String name, String email) {
        User user = new User(name, email, "password");
        user.setCreatedAt(Instant.now());
        user.setCreatedBy("system");  // Set a default value for createdBy
        return entityManager.persist(user);
    }

    private Category createAndPersistCategory(String name, User owner, boolean isDefault) {
        Category category = new Category(name, owner, isDefault);
        category.setCreatedAt(Instant.now());
        category.setCreatedBy(owner.getEmail());  // Set createdBy to owner's email
        return entityManager.persist(category);
    }

    @Test
    void findAllByOwner_Id_shouldReturnCategoriesForUser() {
        // Arrange
        User user1 = createAndPersistUser("John", "john@example.com");
        User user2 = createAndPersistUser("Jane", "jane@example.com");

        Category category1 = createAndPersistCategory("Food", user1, false);
        Category category2 = createAndPersistCategory("Transport", user1, false);
        Category category3 = createAndPersistCategory("Entertainment", user2, false);

        entityManager.flush();
        entityManager.clear();

        // Act
        Page<Category> result = categoryRepository.findAllByOwner_Id(user1.getId(), PageRequest.of(0, 10));

        // Assert
        assertEquals(2, result.getTotalElements());
        assertTrue(result.getContent().stream().anyMatch(c -> c.getName().equals("Food")));
        assertTrue(result.getContent().stream().anyMatch(c -> c.getName().equals("Transport")));
        assertFalse(result.getContent().stream().anyMatch(c -> c.getName().equals("Entertainment")));
    }

    @Test
    void findByIdAndOwner_Id_shouldReturnCategoryForUser() {
        // Arrange
        User user = createAndPersistUser("John", "john@example.com");
        Category category = createAndPersistCategory("Food", user, false);

        entityManager.flush();
        entityManager.clear();

        // Act
        Category result = categoryRepository.findByIdAndOwner_Id(category.getId(), user.getId()).orElse(null);

        // Assert
        assertNotNull(result);
        assertEquals("Food", result.getName());
        assertEquals(user.getId(), result.getOwner().getId());
    }

    @Test
    void existsByNameAndOwner_Id_shouldReturnTrueForExistingCategory() {
        // Arrange
        User user = createAndPersistUser("John", "john@example.com");
        createAndPersistCategory("Food", user, false);

        entityManager.flush();
        entityManager.clear();

        // Act
        boolean exists = categoryRepository.existsByNameAndOwner_Id("Food", user.getId());

        // Assert
        assertTrue(exists);
    }

    @Test
    void findByNameAndOwner_Id_shouldReturnCategoryForUserAndName() {
        // Arrange
        User user = createAndPersistUser("John", "john@example.com");
        createAndPersistCategory("Food", user, false);

        entityManager.flush();
        entityManager.clear();

        // Act
        Category result = categoryRepository.findByNameAndOwner_Id("Food", user.getId()).orElse(null);

        // Assert
        assertNotNull(result);
        assertEquals("Food", result.getName());
        assertEquals(user.getId(), result.getOwner().getId());
    }
}