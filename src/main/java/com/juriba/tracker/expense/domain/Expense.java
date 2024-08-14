package com.juriba.tracker.expense.domain;

import com.juriba.tracker.common.domain.AggregateRoot;
import com.juriba.tracker.expense.domain.event.ExpenseCreatedEvent;
import com.juriba.tracker.expense.domain.event.ExpenseUpdatedEvent;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Entity
@Table(name = "expenses")
@Getter
@NoArgsConstructor
public class Expense extends AggregateRoot {

    private String description;

    @Column(nullable = false)
    private BigDecimal amount;


    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    public Expense(String description, BigDecimal amount,  Category category) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        registerEvent(new ExpenseCreatedEvent(this));
    }

    public void update(String description, BigDecimal amount, Category category) {
        this.description = description;
        this.amount = amount;
        this.category = category;
        registerEvent(new ExpenseUpdatedEvent(this));
    }

}