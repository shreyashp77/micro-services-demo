package com.example.product_service.service;

import java.util.List;

import com.example.product_service.dto.ProductRequest;
import com.example.product_service.dto.ProductResponse;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);
    ProductResponse updateProduct(Long id, ProductRequest request);
    void deleteProduct(Long id);
    List<ProductResponse> getAllProducts();
    ProductResponse getProduct(Long id);
    List<ProductResponse> searchProductsByName(String name);
    void updateProductStock(Long productId,
            int quantity);
}