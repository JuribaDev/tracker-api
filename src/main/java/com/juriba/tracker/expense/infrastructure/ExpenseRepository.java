package com.juriba.tracker.expense.infrastructure;

import com.juriba.tracker.expense.domain.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;
import java.util.List;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, String> {
    Page<Expense> findByOwner_IdAndCreatedAtBetween(String ownerId,Instant startDate, Instant endDate, Pageable pageable);
    Page<Expense> findAllByOwner_Id(String ownerId,Pageable pageable);

    List<Expense> findAllByCategory_Id(String categoryId);
}
