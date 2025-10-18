package com.example.userservice.exception;

/**
 * Exception thrown when a user is not found in the system.
 * This exception is used when trying to access or modify a user that doesn't
 * exist.
 */
public class UserNotFoundException extends RuntimeException {
    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @param message the detail message
     */
    public UserNotFoundException(String message) {
        super(message);
    }
}
