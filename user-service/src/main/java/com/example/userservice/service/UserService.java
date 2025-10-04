package com.example.userservice.service;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.entity.User;
import com.example.userservice.exception.UserAlreadyExistsException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UserService {
    private final UserRepository userRepo;

    public UserService(UserRepository repo) {
        this.userRepo = repo;
    }

    public UserDTO createUser(UserDTO dto) {
        if(userRepo.existsByEmail(dto.getEmail())) {
            throw new UserAlreadyExistsException("User with email " + dto.getEmail() + " already exists.");
        }

        User user = new User(dto.getName(), dto.getEmail());
        User savedUser = userRepo.save(user);

        return new UserDTO(savedUser.getName(), savedUser.getEmail());
    }

    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream().map(u -> new UserDTO(u.getName(), u.getEmail())).collect(Collectors.toList());
    }

    public User updateUser(Long id, UserDTO updatedUser) {
        return userRepo.findById(id).map(u -> {
            u.setName(updatedUser.getName());
            u.setEmail(updatedUser.getEmail());
            return userRepo.save(u);
        }).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    public void deleteUser(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        userRepo.delete(user);
    }
}
