package com.example.userservice.entity;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

/**
 * Entity class representing a user in the system.
 * Maps to the 'app_user' table in the database.
 */
@Entity
@Table(name = "app_user")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User implements java.io.Serializable {
    private static final long serialVersionUID = 1L;

    /**
     * Unique identifier for the user.
     * Generated automatically using UUID strategy.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    /**
     * The name of the user.
     */
    String name;

    /**
     * The email address of the user.
     */
    String email;

    /**
     * Constructor to create a new user with name and email.
     * 
     * @param name  The name of the user
     * @param email The email address of the user
     */
    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}