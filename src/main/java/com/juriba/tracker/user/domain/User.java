package com.juriba.tracker.user.domain;

import com.juriba.tracker.common.domain.AggregateRoot;
import com.juriba.tracker.user.domain.event.*;
import jakarta.persistence.*;
import lombok.*;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Builder
@Table(name = "users")
@EqualsAndHashCode(callSuper = true)
@Data
@NoArgsConstructor
@AllArgsConstructor
public class User extends AggregateRoot implements UserDetails {
    @Column(unique = true, nullable = false)
    private String email;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles = new HashSet<>();

    private boolean accountNonExpired = true;
    private boolean accountNonLocked = true;
    private boolean credentialsNonExpired = true;
    private boolean enabled = true;



    public User(String email, String password) {
        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.password = password;
        registerEvent(new UserCreatedEvent(this));
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return  roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());
    }

    @Override
    public String getUsername() {
        return email;
    }

    @Override
    public boolean isAccountNonExpired() {
        return accountNonExpired;
    }

    @Override
    public boolean isAccountNonLocked() {
        return accountNonLocked;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return credentialsNonExpired;
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }


    public void changePassword(String newPassword) {
        this.password = newPassword;
        registerEvent(new UserPasswordChangedEvent(this));
    }

    public void addRole(Role role) {
        if (!this.roles.contains(role)) {
            this.roles.add(role);
            registerEvent(new UserRoleAddedEvent(this, role));
        }
    }

    public void removeRole(Role role) {
        if (this.roles.remove(role)) {
            registerEvent(new UserRoleRemovedEvent(this, role));
        }
    }

    public void disable() {
        this.enabled = false;
        registerEvent(new UserDisabledEvent(this));
    }

    public void enable() {
        this.enabled = true;
        registerEvent(new UserEnabledEvent(this));
    }

    public void lockAccount() {
        this.accountNonLocked = false;
        registerEvent(new UserAccountLockedEvent(this));
    }

    public void unlockAccount() {
        this.accountNonLocked = true;
        registerEvent(new UserAccountUnlockedEvent(this));
    }
}