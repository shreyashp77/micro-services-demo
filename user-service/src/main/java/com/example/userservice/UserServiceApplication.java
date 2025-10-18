package com.example.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.cloud.client.discovery.EnableDiscoveryClient;

/**
 * Main application class for the User Service.
 * This is the entry point for the Spring Boot application.
 * 
 * Features:
 * - Spring Boot application
 * - Caching support enabled
 * - Service discovery client for Eureka
 */
@SpringBootApplication
@EnableCaching
@EnableDiscoveryClient
public class UserServiceApplication {
    /**
     * Main method to run the Spring Boot application.
     * 
     * @param args Command line arguments
     */
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
