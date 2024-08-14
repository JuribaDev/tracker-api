package com.juriba.tracker.auth.application.imp;

import com.juriba.tracker.auth.application.GetAuthenticatedUserUseCase;
import com.juriba.tracker.common.application.UseCase;
import com.juriba.tracker.common.domain.exception.ResourceNotFoundException;
import com.juriba.tracker.user.domain.User;
import com.juriba.tracker.user.infrastructure.UserRepository;
import org.springframework.security.core.context.SecurityContextHolder;

@UseCase
public class GetAuthenticatedUserUseCaseImp implements GetAuthenticatedUserUseCase {
    private final UserRepository userRepository;
    public GetAuthenticatedUserUseCaseImp(UserRepository userRepository) {
        this.userRepository = userRepository;

    }

    @Override
    public User execute() {
        String email = SecurityContextHolder.getContext().getAuthentication().getName();
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }
}
