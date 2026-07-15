package com.rohit.ecommerce.service;

import com.rohit.ecommerce.dto.ProductRequestDTO;
import com.rohit.ecommerce.dto.ProductResponseDTO;
import com.rohit.ecommerce.entity.Product;
import com.rohit.ecommerce.exception.ResourceNotFoundException;
import com.rohit.ecommerce.repository.ProductRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepo productRepo;

    public ProductResponseDTO createProduct(ProductRequestDTO request) {
        Product product = Product.builder()
                .name(request.getName())
                .description(request.getDescription())
                .price(request.getPrice())
                .stockQuantity(request.getStockQuantity())
                .category(request.getCategory())
                .build();
        Product saved = productRepo.save(product);
        return toResponseDTO(saved);
    }

    private ProductResponseDTO toResponseDTO(Product product) {
        return ProductResponseDTO.builder()
                .id(product.getId())
                .name(product.getName())
                .description(product.getDescription())
                .price(product.getPrice())
                .stockQuantity(product.getStockQuantity())
                .category(product.getCategory())
                .build();
    }

    public List<ProductResponseDTO> getAllProducts() {
        return productRepo.findAll().stream()
                .map(this::toResponseDTO)
                .toList();
    }

    public ProductResponseDTO getProductByID(Long id) {
        Product product =   findProductOrThrow(id);
        return toResponseDTO(product);
    }

    private Product findProductOrThrow(Long id) {
        return productRepo.findById(id)
                .orElseThrow(()-> ResourceNotFoundException.forEntity("Product",id));
    }
}
