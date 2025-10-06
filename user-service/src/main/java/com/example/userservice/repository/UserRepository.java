package com.example.userservice.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.example.userservice.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, String> {
    boolean existsByEmail(String email);  // Optimization: quickly check duplicates
}