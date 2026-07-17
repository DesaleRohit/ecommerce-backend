package com.ecommerce.api.controller;

import com.ecommerce.api.dto.AddToCartRequest;
import com.ecommerce.api.entity.CartItem;
import com.ecommerce.api.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/cart")
public class CartController {

    private CartService cartService;

    public  CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @GetMapping
    public Map<String, Object> viewCart() {
        return buildCartResponse(cartService.getCartItems());
    }

    @PostMapping("/items")
    public ResponseEntity<Map<String, Object>> addItem(@Valid @RequestBody AddToCartRequest request) {
        List<CartItem> items = cartService.addToCart(request.getProductId(), request.getQuantity());
        return ResponseEntity.status(HttpStatus.CREATED).body(buildCartResponse(items));
    }

    private Map<String, Object> buildCartResponse(List<CartItem> items) {
        Map<String, Object> response = new HashMap<>();
        response.put("items", items);
        response.put("totalPrice", cartService.calculateCartTotal());
        return response;
    }

}
