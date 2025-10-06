package com.example.userservice.dto;

public record UserResponse (
    String id, 
    String name, 
    String email
) {}