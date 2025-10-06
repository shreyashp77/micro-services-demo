package com.example.userservice.service;

import java.util.List;

import com.example.userservice.dto.UserRequest;
import com.example.userservice.entity.User;

public interface UserService {
    User createUser(UserRequest dto);
    List<User> getAllUsers();
    User getUserById(String id);
    User updateUser(String id, UserRequest updatedUser);
    void deleteUser(String id);
}