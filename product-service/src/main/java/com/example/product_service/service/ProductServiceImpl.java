package com.example.product_service.service;

import com.example.product_service.dto.ProductRequest;
import com.example.product_service.dto.ProductResponse;
import com.example.product_service.exception.ProductNotFoundException;
import com.example.product_service.mapper.ProductMapper;
import com.example.product_service.model.Product;
import com.example.product_service.repository.ProductRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

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
    public ProductResponse updateProduct(Long id, ProductRequest request) {
        Product found = repo.findById(id).orElseThrow(() -> new ProductNotFoundException("Product with id " + id + " not found."));

        found.setName(request.name());
        found.setDescription(request.description());
        found.setPrice(request.price());
        found.setQuantity(request.quantity());

        return mapper.toResponse(repo.save(found));
    }

    @Override
    public void deleteProduct(Long id) {
        if(!repo.existsById(id))
            throw new ProductNotFoundException("Product with id " + id + " not found.");
        repo.deleteById(id);
    }

    @Override
    public List<ProductResponse> getAllProducts() {
        return repo.findAll().stream().map(mapper::toResponse).collect(Collectors.toList());
    }

    @Override
    public ProductResponse getProduct(Long id) {
        return mapper.toResponse(
                repo.findById(id).
                        orElseThrow(
                                () -> new ProductNotFoundException("Product with id " + id + " not found.")
                        )
        );
    }

    @Override
    public List<ProductResponse> searchProductsByName(String name) {
        return repo.findByNameContainingIgnoreCase(name).stream().map(mapper::toResponse).toList();
    }
}