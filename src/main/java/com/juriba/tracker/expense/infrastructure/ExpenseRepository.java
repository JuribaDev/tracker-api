package com.juriba.tracker.expense.infrastructure;

import com.juriba.tracker.expense.domain.Expense;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.Instant;

@Repository
public interface ExpenseRepository extends JpaRepository<Expense, String> {
    Page<Expense> findByCreatedAtBetween(Instant startDate, Instant endDate, Pageable pageable);
    Page<Expense> findAll(Pageable pageable);
}
