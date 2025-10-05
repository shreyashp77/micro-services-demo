package com.example.product_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;

public record OrderEvent (

    @NotNull(message = "Product ID is mandatory")
    Long productId,
    @Min(value = 1, message = "Minimum quantity is 1")
    int quantity,
    @NotNull(message = "User ID is mandatory")
    Long userId
) {} 