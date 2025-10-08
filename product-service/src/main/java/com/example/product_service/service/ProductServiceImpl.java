package com.example.product_service.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import com.example.product_service.dto.ProductRequest;
import com.example.product_service.dto.ProductResponse;
import com.example.product_service.exception.ProductNotFoundException;
import com.example.product_service.mapper.ProductMapper;
import com.example.product_service.model.Product;
import com.example.product_service.repository.ProductRepository;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository repo;
    private final ProductMapper mapper;

    public ProductServiceImpl(ProductRepository repo, ProductMapper mapper) {
        this.repo = repo;
        this.mapper = mapper;
    }

    @Override
    public ProductResponse createProduct(ProductRequest request) {
        return mapper.toResponse(repo.save(mapper.toEntity(request)));
    }

    @Override
    // Cache product data when updating to keep cache consistent
    @CachePut(value = "products", key = "#id")
    public ProductResponse updateProduct(String id, ProductRequest request) {
        Product found = repo.findById(id)
                .orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found."));

        found.setName(request.name());
        found.setDescription(request.description());
        found.setPrice(request.price());
        found.setQuantity(request.quantity());

        return mapper.toResponse(repo.save(found));
    }

    @Override
    // Evict the cache when a product is deleted
    @CacheEvict(value = "products", key = "#id")
    public void deleteProduct(String id) {
        if (!repo.existsById(id))
            throw new ProductNotFoundException("Product with id " + id + " not found.");
        repo.deleteById(id);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return repo.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Override
    // Cache product data by id to improve performance
    @Cacheable(value = "products", key = "#id")
    public ProductResponse getProduct(String id) {
        return mapper.toResponse(
                repo.findById(id).orElseThrow(
                        () -> new ProductNotFoundException("Product with id " + id + " not found.")));
    }

    @Override
    // Cache product data by name to improve performance
    @Cacheable(value = "products", key = "#name")
    public List<ProductResponse> searchProductsByName(String name) {
        return repo.findByNameContainingIgnoreCase(name).stream().map(mapper::toResponse).toList();
    }

    @Override
    // Cache product data when updating to keep cache consistent
    @CachePut(value = "products", key = "#id")
    public ProductResponse updateProductStock(String id, int quantity) {
        return repo.findById(id).map(product -> {
            int newQuantity = product.getQuantity() - quantity;
            if (newQuantity < 0) {
                throw new IllegalArgumentException("Insufficient stock for product ID: " + id);
            }
            product.setQuantity(newQuantity);
            return mapper.toResponse(repo.save(product));
        }).orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found."));
    }
}