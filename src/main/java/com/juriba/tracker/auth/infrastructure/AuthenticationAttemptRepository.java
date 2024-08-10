package com.juriba.tracker.auth.infrastructure;

import com.juriba.tracker.auth.domain.AuthenticationAttempt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuthenticationAttemptRepository extends JpaRepository<AuthenticationAttempt, String> {
}
