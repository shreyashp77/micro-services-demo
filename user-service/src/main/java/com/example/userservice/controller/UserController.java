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

@RestController
@RequestMapping("/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    ResponseEntity<List<UserResponse>> getAllUsers() {
        return ResponseEntity.ok(userService.getAllUsers().stream().map(u -> new UserResponse(u.getId(), u.getName(), u.getEmail())).toList());
    }

    @GetMapping("/{id}")
    ResponseEntity<UserResponse> getUserById(@PathVariable String id) {
        User user = userService.getUserById(id);
        return ResponseEntity.ok(new UserResponse(user.getId(), user.getName(), user.getEmail()));
    }

    @PostMapping
    ResponseEntity<UserResponse> createUser(@RequestBody UserRequest userToCreate) {
        User createdUser = userService.createUser(userToCreate);
        return new ResponseEntity<>(new UserResponse(createdUser.getId(), createdUser.getName(), createdUser.getEmail()), HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    ResponseEntity<UserResponse> updateUser(@PathVariable String id, @RequestBody UserRequest userToUpdate) {
        User updatedUser = userService.updateUser(id, userToUpdate);
        return ResponseEntity.ok(new UserResponse(updatedUser.getId(), updatedUser.getName(), updatedUser.getEmail()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable String id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build(); // 204 No Content
    }
}