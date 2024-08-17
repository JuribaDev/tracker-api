package com.juriba.tracker.expense.domain;

import com.juriba.tracker.common.domain.AggregateRoot;
import com.juriba.tracker.expense.domain.event.ExpenseCreatedEvent;
import com.juriba.tracker.expense.domain.event.ExpenseDeletedEvent;
import com.juriba.tracker.expense.domain.event.ExpenseUpdatedEvent;
import com.juriba.tracker.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;

@Entity
@Table(name = "expenses",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name", "category_id"}),
                @UniqueConstraint(columnNames = {"name", "owner_id"})
        },

        indexes = {
                @Index(name = "idx_expense_category", columnList = "category_id"),
                @Index(name = "idx_expense_owner", columnList = "owner_id"),
        })

@Getter
@Setter
@NoArgsConstructor
public class Expense extends AggregateRoot {

    private String description;

    @Column(nullable = false)
    private BigDecimal amount;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;

    public Expense(String description, BigDecimal amount,  Category category, User owner) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        this.owner = owner;
        registerEvent(new ExpenseCreatedEvent(this));
    }

    public void update(String description, BigDecimal amount, Category category) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        registerEvent(new ExpenseUpdatedEvent(this));
    }


    public void deleteExpense() {
        registerEvent(new ExpenseDeletedEvent(this));
    }
}