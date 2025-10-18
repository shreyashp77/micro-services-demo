package com.example.userservice.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Security configuration class for the user service.
 * This configuration disables CSRF protection and permits all requests
 * without authentication, which is typically used in microservices
 * architectures where authentication is handled by an API gateway.
 */
@Configuration
public class SecurityConfig {

    /**
     * Configures the security filter chain for HTTP requests.
     * Disables CSRF protection and permits all requests without authentication.
     * 
     * @param http the HttpSecurity object to configure
     * @return SecurityFilterChain instance
     * @throws Exception if security configuration fails
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // disable CSRF
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // allow all requests without login
                );
        return http.build();
    }
}
