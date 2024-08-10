package com.juriba.tracker.auth.domain;

import com.juriba.tracker.common.domain.AggregateRoot;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.time.Instant;


@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@Data
@NoArgsConstructor
public class AuthenticationAttempt extends AggregateRoot {
    @Id
    private String id;
    private String email;
    private boolean successful;
    private Instant timestamp;


    public AuthenticationAttempt(String id, String email, boolean successful) {
        this.id = id;
        this.email = email;
        this.successful = successful;
        this.timestamp = Instant.now();
        registerEvent(new AuthenticationAttemptedEvent(this));
    }

}
