package com.example.userservice.dto;

import jakarta.validation.constraints.NotBlank;

public record UserDTO (
    @NotBlank(message = "Name is mandatory")
    String name,

    @NotBlank(message = "Email is mandatory")
    String email
) {}