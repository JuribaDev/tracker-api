package com.juriba.tracker.user.domain.event;

import com.juriba.tracker.common.application.DomainEvent;
import com.juriba.tracker.expense.domain.Category;
import com.juriba.tracker.user.domain.User;
import lombok.Getter;

import java.time.OffsetDateTime;

@Getter
public class UserCategoryAddedEvent implements DomainEvent {
    private final User user;
    private final Category category;
    private final OffsetDateTime occurredOn;
    public UserCategoryAddedEvent(User user, Category category) {
        this.user = user;
        this.category = category;
        this.occurredOn = OffsetDateTime.now();
    }

    @Override
    public OffsetDateTime occurredOn() {
        return occurredOn;
    }
}
