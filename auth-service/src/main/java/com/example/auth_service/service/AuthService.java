package com.example.auth_service.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.example.auth_service.dto.UserDTO;
import com.example.auth_service.entity.User;
import com.example.auth_service.repository.UserRepository;

@Service
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthService(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
    }

    public UserDTO createUser(UserDTO userDTO) {
        User userToSave = new User(
                userDTO.getUsername(),
                passwordEncoder.encode(userDTO.getPassword()));
        userRepository.save(userToSave);
        return new UserDTO(userDTO.getUsername(), userDTO.getPassword());
    }
}