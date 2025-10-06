package com.example.userservice.service;

import java.util.List;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.userservice.dto.UserRequest;
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
    public User createUser(UserRequest dto) {
        if (userRepo.existsByEmail(dto.email())) {
            throw new UserAlreadyExistsException("User with email " + dto.email() + " already exists.");
        }

        User user = new User(dto.name(), dto.email());
        User savedUser = userRepo.save(user);
        return savedUser;
    }

    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    @Override
    // Cache user data by id to improve performance
    @Cacheable(value = "users", key = "#id")
    public User getUserById(String id) {
        return userRepo.findById(id).
                orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    @Override
    // Cache user data when updating to keep cache consistent
    @CachePut(value = "users", key = "#id")
    public User updateUser(String id, UserRequest updatedUser) {
        return userRepo.findById(id).map(u -> {
            u.setName(updatedUser.name());
            u.setEmail(updatedUser.email());
            return userRepo.save(u);
        }).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    @Override
    // Evict the cache when a user is deleted
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(String id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        userRepo.delete(user);
    }
}