package com.juriba.tracker.expense.infrastructure;

import com.juriba.tracker.expense.domain.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface CategoryRepository extends JpaRepository<Category, String> {
    Page<Category> findAllByOwner_Id(String ownerId, Pageable pageable);
    Optional<Category> findByIdAndOwner_Id(String id, String ownerId);
    boolean existsByNameAndOwner_Id(String name, String ownerId);
    Optional<Category> findByNameAndOwner_Id(String name, String ownerId);
}