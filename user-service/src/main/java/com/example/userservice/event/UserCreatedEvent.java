package com.example.userservice.event;

public record UserCreatedEvent(
        Long id,
        String name,
        String email
) {}