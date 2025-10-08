package com.example.product_service.service;

import java.util.List;

import com.example.product_service.dto.ProductRequest;
import com.example.product_service.dto.ProductResponse;

public interface ProductService {
    ProductResponse createProduct(ProductRequest request);

    ProductResponse updateProduct(String id, ProductRequest request);

    void deleteProduct(String id);

    List<ProductResponse> getAllProducts();

    ProductResponse getProduct(String id);

    List<ProductResponse> searchProductsByName(String name);

    ProductResponse updateProductStock(String id, int quantity);
}