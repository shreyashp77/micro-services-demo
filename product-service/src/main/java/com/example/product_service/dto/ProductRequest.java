package com.example.product_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public record ProductRequest(
        @NotBlank(message = "Name is mandatory")
        String name,

        String description,

        @NotNull(message = "Price is mandatory")
        @Min(value = 0, message = "Price must be a positive value")
        Double price,

        @NotNull(message = "Quantity is mandatory")
        @Min(value = 0, message = "Quantity must be a positive value")
        Integer quantity
) {}
