package com.juriba.tracker.auth.domain;

import com.juriba.tracker.auth.domain.event.AuthenticationAttemptedEvent;
import com.juriba.tracker.common.domain.AggregateRoot;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.*;

import java.time.Instant;


@EqualsAndHashCode(callSuper = true)
@Entity
@AllArgsConstructor
@Data
@Builder
@NoArgsConstructor
public class AuthenticationAttempt extends AggregateRoot {
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
