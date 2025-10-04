package com.example.auth_service.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

import com.example.auth_service.entity.User;
import com.example.auth_service.repository.UserRepository;
import com.example.auth_service.service.CustomUserDetailsService;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    public SecurityConfig(CustomUserDetailsService userDetailsService) {
        this.userDetailsService = userDetailsService;
    }

    /**
     * Provides a UserDetailsService bean.
     */
    @Bean
    public UserDetailsService userDetailsService() {
        return userDetailsService;
    }

     /**
     * Provides a PasswordEncoder bean for hashing passwords.
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    
    /**
     * Defines the security filter chain for the application.
     * This is where we configure which endpoints are public and which are
     * protected.
     */
    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(AbstractHttpConfigurer::disable) // Disable CSRF for stateless APIs
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll()       // All requests are kept public here, since gateway handles security
                ) // Use stateless sessions
                .sessionManagement(sess -> sess.sessionCreationPolicy(SessionCreationPolicy.STATELESS));

        return http.build();
    }

    /**
     * Seeds an admin user into the database at application startup if the admin username and password
     * are provided via application.properties file. If the admin user does not already exist, a new user
     * with the specified credentials and the "ROLE_ADMIN" role is created and saved.
     *
     * @param userRepository   the repository used to access and persist User entities
     * @param passwordEncoder  the encoder used to securely hash the admin password
     * @param adminUsername    the username for the admin user, provided via the "admin.seed.username" property
     * @param adminPassword    the password for the admin user, provided via the "admin.seed.password" property
     * @return a CommandLineRunner that performs the seeding logic at application startup
     */
    @Bean
    public CommandLineRunner seedAdminUser(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            @Value("${admin.seed.username:}") String adminUsername,
            @Value("${admin.seed.password:}") String adminPassword) {
        return args -> {
            if (!adminUsername.isBlank() && !adminPassword.isBlank()) {
                if (userRepository.findByUsername(adminUsername).isEmpty()) {
                    User admin = new User();
                    admin.setUsername(adminUsername);
                    admin.setPassword(passwordEncoder.encode(adminPassword));
                    admin.setRole("ROLE_ADMIN");
                    userRepository.save(admin);
                    System.out.println("Admin user seeded.");
                }
            } else {
                System.out.println("Admin seed skipped: username or password not set.");
            }
        };
    }

    /**
     * Exposes the AuthenticationManager from the security configuration as a bean.
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    /**
     * Defines the authentication provider, linking the UserDetailsService and
     * PasswordEncoder.
     */
    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder());
        return authProvider;
    }
}