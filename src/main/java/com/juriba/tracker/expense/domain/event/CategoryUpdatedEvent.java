package com.juriba.tracker.expense.domain.event;

import com.juriba.tracker.common.application.DomainEvent;
import com.juriba.tracker.expense.domain.Category;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
public class CategoryUpdatedEvent implements DomainEvent {
    private final Category category;
    private final OffsetDateTime occurredOn;
    public CategoryUpdatedEvent(Category category) {
        this.category = category;
        this.occurredOn = OffsetDateTime.now();
    }

    @Override
    public OffsetDateTime occurredOn() {
        return occurredOn;
    }
}
