package com.example.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.userservice.entity.User;

/**
 * Repository interface for User entity.
 * Provides data access operations for User entities using Spring Data JPA.
 */
@Repository
public interface UserRepository extends JpaRepository<User, String> {
    /**
     * Checks if a user with the given email already exists.
     * Optimization: quickly check for duplicates before creating a new user.
     * 
     * @param email The email address to check for existence
     * @return true if a user with the email exists, false otherwise
     */
    boolean existsByEmail(String email);
}
