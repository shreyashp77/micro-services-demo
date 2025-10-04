package com.example.auth_service.service;

import java.util.ArrayList;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import com.example.auth_service.entity.User;
import com.example.auth_service.repository.UserRepository;

/**
 * Service implementation for loading user-specific data.
 * <p>
 * This class implements the {@link org.springframework.security.core.userdetails.UserDetailsService}
 * interface to provide custom logic for retrieving user details from the database.
 * </p>
 * 
 * <p>
 * It uses the {@link UserRepository} to fetch user information based on the provided username.
 * If the user is not found, a {@link org.springframework.security.core.userdetails.UsernameNotFoundException}
 * is thrown.
 * </p>
 * 
 * <p>
 * The returned {@link org.springframework.security.core.userdetails.User} object contains the username,
 * password, and an empty list of authorities.
 * </p>
 * 
 */
@Service
public class CustomUserDetailsService implements UserDetailsService {

    @Autowired
    private UserRepository userRepository;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("User not found with username: " + username));

        return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());
    }
}