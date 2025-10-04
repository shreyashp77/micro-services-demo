package com.example.userservice.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.entity.User;
import com.example.userservice.exception.UserAlreadyExistsException;
import com.example.userservice.exception.UserNotFoundException;
import com.example.userservice.repository.UserRepository;

@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;

    public UserServiceImpl(UserRepository repo) {
        this.userRepo = repo;
    }

    @Override
    public UserDTO createUser(UserDTO dto) {
        if(userRepo.existsByEmail(dto.email())) {
            throw new UserAlreadyExistsException("User with email " + dto.email() + " already exists.");
        }

        User user = new User(dto.name(), dto.email());
        User savedUser = userRepo.save(user);

        return new UserDTO(savedUser.getName(), savedUser.getEmail());
    }

    @Override
    public List<UserDTO> getAllUsers() {
        return userRepo.findAll().stream().map(u -> new UserDTO(u.getName(), u.getEmail())).collect(Collectors.toList());
    }

    @Override
    public User updateUser(Long id, UserDTO updatedUser) {
        return userRepo.findById(id).map(u -> {
            u.setName(updatedUser.name());
            u.setEmail(updatedUser.email());
            return userRepo.save(u);
        }).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    @Override
    public void deleteUser(Long id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        userRepo.delete(user);
    }
}