package com.example.userservice.dto;

import jakarta.validation.constraints.NotBlank;

/**
 * Data Transfer Object for creating a new user.
 * Contains the required fields for user creation.
 */
public record UserRequest(
        /**
         * The name of the user. Must not be blank.
         */
        @NotBlank(message = "Name is mandatory") String name,

        /**
         * The email address of the user. Must not be blank.
         */
        @NotBlank(message = "Email is mandatory") String email) {
}
