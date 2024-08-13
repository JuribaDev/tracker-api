package com.juriba.tracker.auth.application.imp;

import com.juriba.tracker.user.infrastructure.UserRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedUserDetailServiceImp implements UserDetailsService {
    private final UserRepository userRepository;

    public AuthenticatedUserDetailServiceImp(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String email){
        return userRepository.findByEmail(email).orElseThrow(() -> new  UsernameNotFoundException("User not found: " + email));
    }
}
