package com.example.product_service.dto;

public record ProductResponse (
        String id,
        String name,
        String description,
        Double price,
        Integer quantity
) implements java.io.Serializable {}