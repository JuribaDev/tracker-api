package com.juriba.tracker.expense.domain.event;

import com.juriba.tracker.common.application.DomainEvent;
import com.juriba.tracker.expense.domain.Expense;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
public class ExpenseCreatedEvent implements DomainEvent {
    private final Expense expense;
    private final OffsetDateTime occurredOn;
    public ExpenseCreatedEvent(Expense expense) {
        this.expense = expense;
        this.occurredOn = OffsetDateTime.now();
    }

    @Override
    public OffsetDateTime occurredOn() {
        return occurredOn;
    }
}
