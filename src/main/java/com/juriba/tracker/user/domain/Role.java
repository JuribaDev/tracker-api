package com.juriba.tracker.user.domain;

import com.juriba.tracker.common.domain.AggregateRoot;
import com.juriba.tracker.user.domain.event.RoleCreatedEvent;
import com.juriba.tracker.user.domain.event.RoleNameChangedEvent;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Table(name = "roles")
@EqualsAndHashCode(callSuper = true)
@Builder
@Getter
@NoArgsConstructor
public class Role extends AggregateRoot {
    @Column(unique = true, nullable = false)
    private String name;

    public Role(String name) {
        this.name = name;
        registerEvent(new RoleCreatedEvent(this));
    }

    public void changeName(String newName) {
        this.name = newName;
        registerEvent(new RoleNameChangedEvent(this, newName));
    }
}
