package com.example.product_service.controller;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.product_service.dto.ProductRequest;
import com.example.product_service.dto.ProductResponse;
import com.example.product_service.service.ProductService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService service;

    public ProductController(ProductService service) {
        this.service = service;
    }

    @PostMapping
    ResponseEntity<ProductResponse> createProduct(@Valid @RequestBody ProductRequest req) {
        return new ResponseEntity<>(service.createProduct(req), HttpStatus.CREATED);
    }

    @GetMapping("/{id}")
    ResponseEntity<ProductResponse> getProductById(@PathVariable String id) {
        return ResponseEntity.ok(service.getProduct(id));
    }

    @GetMapping
    ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(service.getAllProducts());
    }

    @PatchMapping("/{id}")
    ResponseEntity<ProductResponse> updateProduct(@PathVariable String id, @RequestBody ProductRequest req) {
        return new ResponseEntity<>(service.updateProduct(id, req), HttpStatus.NO_CONTENT);
    }

    @DeleteMapping("/{id}")
    ResponseEntity<Void> deleteProduct(@PathVariable String id) {
        service.deleteProduct(id);
        return ResponseEntity.noContent().build();
    }
}