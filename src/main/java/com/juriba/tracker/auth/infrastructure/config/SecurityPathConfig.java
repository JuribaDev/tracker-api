package com.juriba.tracker.auth.infrastructure.config;


import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;
import org.springframework.security.web.util.matcher.OrRequestMatcher;
import org.springframework.security.web.util.matcher.RequestMatcher;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Configuration
public class SecurityPathConfig {

    protected static final List<String> PUBLIC_PATHS = Arrays.asList(
            "/api/v1/auth/**",
            "/swagger-ui/**",
            "/v1/api-docs/**",
            "/actuator/**",
            "/h2-console/**",
            "/api/v1/roles/**"
    );

    public RequestMatcher publicPathsMatcher() {
        return new OrRequestMatcher(
                PUBLIC_PATHS.stream()
                        .map(AntPathRequestMatcher::new)
                        .collect(Collectors.toList())
        );
    }
}
