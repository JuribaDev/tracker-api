package com.juriba.tracker.user.domain;

import com.juriba.tracker.common.domain.AggregateRoot;
import com.juriba.tracker.user.domain.event.RoleCreatedEvent;
import com.juriba.tracker.user.domain.event.RoleNameChangedEvent;
import jakarta.persistence.Entity;
import jakarta.persistence.*;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.OffsetDateTime;
import java.util.HashSet;
import java.util.Set;
import java.util.UUID;

@Entity
@Table(name = "roles",
        indexes = {
                @Index(name = "idx_role_name", columnList = "name", unique = true)
        })
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
