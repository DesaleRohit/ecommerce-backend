package com.ecommerce.api.service;

import com.ecommerce.api.dto.ProductRequest;
import com.ecommerce.api.entity.Products;
import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.repository.ProductRepo;
import jakarta.validation.Valid;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    private ProductRepo productRepo;

    public ProductService(ProductRepo productRepo) {
        this.productRepo = productRepo;
    }

    public List<Products> getAllProducts() {
        return productRepo.findAll();
    }

    public Products createProduct(@Valid ProductRequest request) {
        Products products = new Products();
        products.setName(request.getName());
        products.setDescription(request.getDescription());
        products.setPrice(request.getPrice());
        products.setStockQuantity(request.getStockQuantity());
        products.setCategory(request.getCategory());
        return productRepo.save(products);
    }

    public Products getProductById(Long id) {
        return productRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Product not found with id:"+id));
    }
}
