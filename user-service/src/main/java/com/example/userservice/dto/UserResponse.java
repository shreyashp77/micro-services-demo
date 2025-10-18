package com.example.userservice.dto;

/**
 * Data Transfer Object for returning user information.
 * Used for responding to API requests with user data.
 */
public record UserResponse(
        /**
         * The unique identifier of the user.
         */
        String id,

        /**
         * The name of the user.
         */
        String name,

        /**
         * The email address of the user.
         */
        String email) {
}
