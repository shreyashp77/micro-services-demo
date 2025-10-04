package com.example.auth_service.dto;

// Using a record for a simple, immutable Data Transfer Object
public record AuthRequest(String username, String password) {}