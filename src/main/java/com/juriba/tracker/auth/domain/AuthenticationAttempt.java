package com.juriba.tracker.auth.domain;

import com.juriba.tracker.auth.domain.event.AuthenticationAttemptedEvent;
import com.juriba.tracker.common.domain.AggregateRoot;
import jakarta.persistence.*;
import lombok.*;

import java.time.Instant;
import java.time.OffsetDateTime;
import java.util.UUID;


@EqualsAndHashCode(callSuper = true)
@Entity
@Data
@Builder
@NoArgsConstructor
@Table(name = "authentication_attempts")
public class AuthenticationAttempt extends AggregateRoot {
    private String email;
    private boolean successful;


    public AuthenticationAttempt( String email, boolean successful) {
//        this.id = UUID.randomUUID().toString();
        this.email = email;
        this.successful = successful;
//        this.createdBy = "System";
//        this.createdAt = OffsetDateTime.now();
        registerEvent(new AuthenticationAttemptedEvent(this));
    }

}
