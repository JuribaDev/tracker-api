package com.juriba.tracker.expense.domain;

import com.juriba.tracker.common.domain.AggregateRoot;
import com.juriba.tracker.expense.domain.event.CategoryCreatedEvent;
import com.juriba.tracker.expense.domain.event.CategoryDeletedEvent;
import com.juriba.tracker.expense.domain.event.CategoryUpdatedEvent;
import com.juriba.tracker.user.domain.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "categories",
        uniqueConstraints = {
                @UniqueConstraint(columnNames = {"name", "owner_id"})
        },
        indexes = {
                @Index(name = "idx_category_owner", columnList = "owner_id"),
                @Index(name = "idx_category_name", columnList = "name"),
                @Index(name = "idx_category_is_default", columnList = "is_default")})
@Getter
@Setter
@NoArgsConstructor
public class Category extends AggregateRoot {

    @Column(nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private User owner;

    @OneToMany(mappedBy = "category", fetch = FetchType.LAZY)
    private Set<Expense> expenses = new HashSet<>();

    @Column(nullable = false)
    private boolean isDefault;

    public Category(String name, User owner, boolean isDefault) {
        this.name = name;
        this.owner = owner;
        this.isDefault = isDefault;
        registerEvent(new CategoryCreatedEvent(this));
    }

    public void updateCategory(String name) {
        this.name = name;
        registerEvent(new CategoryUpdatedEvent(this));
    }

    public void deleteCategory() {
        if (!this.name.equals("UnLinked")) {
            registerEvent(new CategoryDeletedEvent(this));
        }
    }

}