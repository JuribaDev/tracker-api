package com.juriba.tracker.expense.infrastructure;

import com.juriba.tracker.expense.domain.Category;
import com.juriba.tracker.user.domain.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Optional<Category> findByName(String name);
    boolean existsByName(String name);

    boolean existsByNameAndOwner( String name, User currentUser);

    Optional<Category> findByNameAndOwner(String name, User owner);
}