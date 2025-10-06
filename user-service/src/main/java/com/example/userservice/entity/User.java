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

@Entity
@Table(name = "app_user")
@Getter
@Setter
@ToString
@NoArgsConstructor
public class User implements java.io.Serializable {
    private static final long serialVersionUID = 1L;
    
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    String name;
    String email;

    public User(String name, String email) {
        this.name = name;
        this.email = email;
    }
}