package com.juriba.tracker.user.domain;

import com.juriba.tracker.common.domain.AggregateRoot;
import com.juriba.tracker.user.domain.event.RoleCreatedEvent;
import com.juriba.tracker.user.domain.event.RoleNameChangedEvent;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "roles")
@EqualsAndHashCode(callSuper = true)
@Getter
@NoArgsConstructor
public class Role extends AggregateRoot {
    @Column(unique = true, nullable = false)
    private String name;
    @ManyToMany(mappedBy = "roles")
    private final Set<User> users = new HashSet<>();

    public Role(String name) {
        this.name = name;
        registerEvent(new RoleCreatedEvent(this));
    }

    public void changeName(String newName) {
        this.name = newName;
        registerEvent(new RoleNameChangedEvent(this, newName));
    }
}
