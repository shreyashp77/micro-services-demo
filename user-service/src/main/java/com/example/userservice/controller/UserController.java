package com.example.userservice.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.dto.UserResponse;
import com.example.userservice.entity.User;
import com.example.userservice.service.UserService;

/**
 * REST controller for managing user-related operations.
 * Provides endpoints for creating, reading, updating, and deleting users.
 */
@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    /**
     * Constructor for dependency injection of UserService.
     * 
     * @param userService The service layer implementation for user operations
     */
    public UserController(UserService userService) {
        this.userService = userService;
    }

    /**
     * Retrieves all users from the database.
     * 
     * @return ResponseEntity containing a list of UserResponse objects
     */
    @GetMapping
    ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers().stream()
                .map(u -> new UserResponse(u.getId(), u.getName(), u.getEmail())).toList());
    }

    /**
     * Retrieves a specific user by ID.
     * 
     * @param id The unique identifier of the user to retrieve
     * @return ResponseEntity containing the UserResponse object if found, or 404 if
     *         not found
     */
    @GetMapping("/{id}")
    ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(new UserResponse(user.getId(), user.getName(), user.getEmail()));
    }

    /**
     * Creates a new user with the provided details.
     * 
     * @param userToCreate The user details to create
     * @return ResponseEntity containing the created UserResponse object with 201
     *         status
     */
    @PostMapping
    ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userToCreate) {
        User createdUser = userService.createUser(userToCreate);
        return new ResponseEntity<>(
                new UserResponse(createdUser.getId(), createdUser.getName(), createdUser.getEmail()),
                HttpStatus.CREATED);
    }

    /**
     * Updates an existing user with the provided details.
     * 
     * @param id           The unique identifier of the user to update
     * @param userToUpdate The updated user details
     * @return ResponseEntity containing the updated UserResponse object
     */
    @PutMapping("/{id}")
    ResponseEntity<UserResponse> updateUser(@PathVariable String id, @RequestBody UserRequest userToUpdate) {
        User updatedUser = userService.updateUser(id, userToUpdate);
        return ResponseEntity.ok(new UserResponse(updatedUser.getId(), updatedUser.getName(), updatedUser.getEmail()));
    }

    /**
     * Deletes a user by ID.
     * 
     * @param id The unique identifier of the user to delete
     * @return ResponseEntity with 204 No Content status
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}
