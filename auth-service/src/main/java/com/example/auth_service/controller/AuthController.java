package com.example.auth_service.controller;

import java.nio.charset.StandardCharsets;
import java.util.Base64;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.auth_service.dto.AuthRequest;
import com.example.auth_service.dto.UserDTO;
import com.example.auth_service.service.AuthService;
import com.example.auth_service.service.CustomUserDetailsService;
import com.example.auth_service.service.JwtService;

@RestController
@RequestMapping("/auth")
public class AuthController {

    private static final String AUTH_HEADER_PREFIX = "Basic ";
    private static final String AUTH_HEADER_NAME = "Authorization";

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private JwtService jwtService;

    @Autowired
    private CustomUserDetailsService userDetailsService;

    @Autowired
    private AuthService authService;

    /**
     * Endpoint to authenticate a user and generate a JWT.
     * 
     * @param authRequest DTO containing the username and password.
     * @return A JWT string if authentication is successful.
     */
    @PostMapping("/token")
    public ResponseEntity<String> authenticateAndGetToken(@RequestHeader(AUTH_HEADER_NAME) String authHeader) {

        if (authHeader == null || !authHeader.startsWith(AUTH_HEADER_PREFIX)) {
            return new ResponseEntity<>(
                "Missing or Invalid Authorization header",
                HttpStatus.UNAUTHORIZED
            );
        }

        final String[] values = decodeCredentials(authHeader);

        String username = values[0];
        String password = values[1];

        return ResponseEntity.ok(generateAuthToken(username, password));
    }

    /**
     * Endpoint to register a new admin user
     * 
     * @param authRequest DTO containing the username and password.
     * @return A JWT string if registration and authentication are successful.
     */
    @PostMapping("/register")
    public ResponseEntity<String> registerAdminUser(@RequestBody AuthRequest authRequest) {
        UserDTO createdUser = authService.createUser(
                new com.example.auth_service.dto.UserDTO(
                        authRequest.username(), authRequest.password()));

        return ResponseEntity.ok(
                    "User registered, generated token:  \n" 
                    + generateAuthToken(
                                createdUser.getUsername(), createdUser.getPassword()
                    )
                );
    }

    private String[] decodeCredentials(String authHeader) {
        // Decode Base64 encoded username:password
        String base64Credentials = authHeader.substring(AUTH_HEADER_PREFIX.length()).trim();
        byte[] credDecoded = Base64.getDecoder().decode(base64Credentials);
        String credentials = new String(credDecoded, StandardCharsets.UTF_8);

        // credentials = username:password
        final String[] values = credentials.split(":", 2);
        return values;
    }

    private String generateAuthToken(String username, String password) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(username, password));

        if (authentication.isAuthenticated()) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            return jwtService.generateToken(userDetails);
        } else {
            throw new RuntimeException("Invalid user request!");
        }
    }
}