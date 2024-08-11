package com.juriba.tracker.auth.infrastructure.config;

import com.juriba.tracker.auth.application.imp.AuthenticatedUserDetailServiceImp;
import com.juriba.tracker.auth.infrastructure.security.JwtAuthenticationFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jose.jwk.RSAKey;
import com.nimbusds.jose.jwk.source.ImmutableJWKSet;
import com.nimbusds.jose.jwk.source.JWKSource;
import com.nimbusds.jose.proc.SecurityContext;

import java.security.interfaces.RSAPrivateKey;
import java.security.interfaces.RSAPublicKey;

// Indicates that this class is a configuration class
@Configuration
// Enables Spring Security's web security support
@EnableWebSecurity
public class SecurityConfig {

    // Injects the public key for JWT verification from application properties
    @Value("${jwt.public.key}")
    RSAPublicKey publicKey;

    // Injects the private key for JWT signing from application properties
    @Value("${jwt.private.key}")
    RSAPrivateKey privateKey;

    // UserDetailsService for loading user-specific data
    private final AuthenticatedUserDetailServiceImp userDetailsService;

    // Constructor injection of UserDetailsService
    public SecurityConfig(AuthenticatedUserDetailServiceImp userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    // Defines the security filter chain
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                // Configures authorization rules
                .authorizeHttpRequests(authorizeRequests -> authorizeRequests
                        // Permits all requests to these endpoints without authentication
                        .requestMatchers("/api/v1/auth/**", "/swagger-ui/**", "/v3/api-docs/**", "/h2-console/**","/").permitAll()
                        // All other requests require authentication
                        .anyRequest().authenticated()
                )
                // Configures CSRF protection
                .csrf(csrf -> csrf
                        // Disables CSRF protection for H2 console
                        .ignoringRequestMatchers("/h2-console/**")
                )
                // Configures header options
                .headers(headers -> headers
                        // Disables X-Frame-Options for H2 console
                        .frameOptions(op -> op.disable())
                )
                // Configures session management
                .sessionManagement(session -> session
                        // Sets the session creation policy to stateless
                        .sessionCreationPolicy(SessionCreationPolicy.STATELESS)
                )
                // Adds the custom JWT authentication filter before UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthenticationFilter(), UsernamePasswordAuthenticationFilter.class)
                // Configures exception handling
                .exceptionHandling(exceptions -> exceptions
                        // Sets custom authentication entry point for unauthorized requests
                        .authenticationEntryPoint((request, response, authException) -> {
                            response.sendError(401, "Unauthorized: JWT token is missing or invalid");
                        })
                );

        return http.build();
    }

    // Creates a bean for the custom JWT authentication filter
    @Bean
    public JwtAuthenticationFilter jwtAuthenticationFilter() {
        // Returns a new instance of JwtAuthenticationFilter with injected dependencies
        return new JwtAuthenticationFilter(jwtDecoder(), userDetailsService);
    }

    // Creates a bean for JWT decoder
    @Bean
    JwtDecoder jwtDecoder() {
        // Creates a JWT decoder using the public key
        return NimbusJwtDecoder.withPublicKey(this.publicKey).build();
    }

    // Creates a bean for JWT encoder
    @Bean
    JwtEncoder jwtEncoder() {
        // Creates a JWK (JSON Web Key) using the public and private keys
        JWK jwk = new RSAKey.Builder(this.publicKey).privateKey(this.privateKey).build();
        // Creates a JWK source with the single key
        JWKSource<SecurityContext> jwks = new ImmutableJWKSet<>(new JWKSet(jwk));
        // Creates and returns a JWT encoder using the JWK source
        return new NimbusJwtEncoder(jwks);
    }

    // Creates a bean for AuthenticationManager
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        // Returns the AuthenticationManager from the AuthenticationConfiguration
        return authenticationConfiguration.getAuthenticationManager();
    }
}