package com.example.userservice.exception;

/**
 * Exception thrown when attempting to create a user that already exists.
 * This exception is used to handle duplicate user creation attempts.
 */
public class UserAlreadyExistsException extends RuntimeException {
    /**
     * Constructs a new exception with the specified detail message.
     * 
     * @param message the detail message
     */
    public UserAlreadyExistsException(String message) {
        super(message);
    }
}
