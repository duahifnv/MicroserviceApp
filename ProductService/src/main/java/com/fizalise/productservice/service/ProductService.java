package com.fizalise.productservice.service;

import com.fizalise.productservice.dto.ProductRequest;
import com.fizalise.productservice.entity.Product;
import com.fizalise.productservice.repository.ProductRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Slf4j
public record ProductService(ProductRepository productRepository) {
    public Product createProduct(ProductRequest productRequest) {
        Product product = Product.builder()
                .name(productRequest.name())
                .description(productRequest.description())
                .price(productRequest.price())
                .build();
        productRepository.save(product);
        log.info("Product created successfully: {}", product);
        return product;
    }
    public List<Product> getAllProducts() {
        return productRepository.findAll();
    }
}
