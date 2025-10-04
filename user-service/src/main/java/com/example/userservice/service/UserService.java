package com.example.userservice.service;

import java.util.List;

import com.example.userservice.dto.UserDTO;
import com.example.userservice.entity.User;

public interface UserService {

    UserDTO createUser(UserDTO dto);

    List<UserDTO> getAllUsers();

    User updateUser(Long id, UserDTO updatedUser);

    void deleteUser(Long id);

}