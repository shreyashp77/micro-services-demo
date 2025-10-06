package com.example.order_service.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;

public record OrderRequest (

    @NotBlank(message = "Product ID is mandatory")
    String productId,
    @Min(value = 1, message = "Minimum quantity is 1")
    int quantity,
    @NotBlank(message = "User ID is mandatory")
    String userId
) {} 