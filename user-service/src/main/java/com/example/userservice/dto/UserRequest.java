package com.example.userservice.dto;

import jakarta.validation.constraints.NotBlank;

public record UserRequest (
    @NotBlank(message = "Name is mandatory")
    String name,

    @NotBlank(message = "Email is mandatory")
    String email
) {}