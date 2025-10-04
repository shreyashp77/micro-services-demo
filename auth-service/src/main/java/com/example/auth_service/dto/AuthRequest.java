package com.example.auth_service.dto;

public record AuthRequest(
    String username, 
    String password
) {}