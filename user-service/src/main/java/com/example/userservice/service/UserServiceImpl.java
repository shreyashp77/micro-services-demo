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

/**
 * Implementation of the UserService interface.
 * Provides business logic for user-related operations with caching support.
 */
@Service
public class UserServiceImpl implements UserService {
    private final UserRepository userRepo;

    /**
     * Constructor for dependency injection of UserRepository.
     * 
     * @param repo The repository for user data access
     */
    public UserServiceImpl(UserRepository repo) {
        this.userRepo = repo;
    }

    /**
     * Creates a new user with the provided details.
     * Checks for duplicate emails before creating the user.
     * 
     * @param dto The user details to create
     * @return The created User entity
     * @throws UserAlreadyExistsException if a user with the same email already
     *                                    exists
     */
    @Override
    public User createUser(UserRequest dto) {
        if (userRepo.existsByEmail(dto.email())) {
            throw new UserAlreadyExistsException("User with email " + dto.email() + " already exists.");
        }

        User user = new User(dto.name(), dto.email());
        User savedUser = userRepo.save(user);
        return savedUser;
    }

    /**
     * Retrieves all users from the database.
     * 
     * @return List of all User entities
     */
    @Override
    public List<User> getAllUsers() {
        return userRepo.findAll();
    }

    /**
     * Retrieves a specific user by ID with caching support.
     * 
     * @param id The unique identifier of the user to retrieve
     * @return The User entity if found
     * @throws UserNotFoundException if user with the given ID is not found
     */
    @Override
    // Cache user data by id to improve performance
    @Cacheable(value = "users", key = "#id")
    public User getUserById(String id) {
        return userRepo.findById(id).orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
    }

    /**
     * Updates an existing user with the provided details.
     * Updates the user data and refreshes the cache to keep it consistent.
     * 
     * @param id          The unique identifier of the user to update
     * @param updatedUser The updated user details
     * @return The updated User entity
     * @throws UserNotFoundException if user with the given ID is not found
     */
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

    /**
     * Deletes a user by ID and evicts the user from cache.
     * 
     * @param id The unique identifier of the user to delete
     * @throws UserNotFoundException if user with the given ID is not found
     */
    @Override
    // Evict the cache when a user is deleted
    @CacheEvict(value = "users", key = "#id")
    public void deleteUser(String id) {
        User user = userRepo.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User with id " + id + " not found"));
        userRepo.delete(user);
    }
}
