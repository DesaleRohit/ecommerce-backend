package com.ecommerce.api.controller;

import com.ecommerce.api.dto.AddToCartRequest;
import com.ecommerce.api.dto.UpdateQuantityRequest;
import com.ecommerce.api.entity.CartItem;
import com.ecommerce.api.service.CartService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @DeleteMapping("/items/{cartItemId}")
    public Map<String, Object> removeItem(@PathVariable Long cartItemId) {
        List<CartItem> items = cartService.removeFormCart(cartItemId);
        return buildCartResponse(items);
    }

    @PutMapping("/items/{cartItemId}")
    public Map<String, Object> updateQuantity(@PathVariable Long cartItemId,
                                              @Valid @RequestBody UpdateQuantityRequest request) {
        List<CartItem> items = cartService.updateQuantity(cartItemId, request.getQuantity());
        return buildCartResponse(items);
    }

    private Map<String, Object> buildCartResponse(List<CartItem> items) {
        Map<String, Object> response = new HashMap<>();
        response.put("items", items);
        response.put("totalPrice", cartService.calculateCartTotal());
        return response;
    }

}
