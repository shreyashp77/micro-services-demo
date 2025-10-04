package com.example.product_service.dto;

public record ProductResponse(
        Long id,
        String name,
        String description,
        Double price,
        Integer quantity
) {}