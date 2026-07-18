package com.ecommerce.api.service;

import com.ecommerce.api.entity.CartItem;
import com.ecommerce.api.entity.Products;
import com.ecommerce.api.exception.InsufficientStockException;
import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.repository.CartItemRepo;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CartService {

    private CartItemRepo cartItemRepo;

    private ProductService productService;

    public CartService(CartItemRepo cartItemRepo, ProductService productService) {
        this.cartItemRepo = cartItemRepo;
        this.productService = productService;
    }

    public List<CartItem> getCartItems() {
        return cartItemRepo.findAll();
    }

    // Adds a product to the cart. If it's already in the cart, we just bump up the quantity
    // instead of creating a second row for the same product.
    public List<CartItem> addToCart(Long productId, int quantity) {
        Products product = productService.getProductById(productId);

        CartItem existingItem = findCartItemByProductId(productId);

        if (existingItem != null) {
            int newQuantity = existingItem.getQuantity() + quantity;
            if (newQuantity > product.getStockQuantity()) {
                throw new InsufficientStockException(
                        "Not enough stock for " + product.getName() + ". Available: " + product.getStockQuantity()
                );
            }
            existingItem.setQuantity(newQuantity);
            cartItemRepo.save(existingItem);
        } else {
            if (quantity > product.getStockQuantity()) {
                throw new InsufficientStockException(
                        "Not enough stock for " + product.getName() + ". Available: " + product.getStockQuantity());
            }
            CartItem newItem = new CartItem(product, quantity);
            cartItemRepo.save(newItem);
        }
        return getCartItems();
    }

    // Adds up price * quantity for every item currently in the cart.
    public double calculateCartTotal() {
        double total = 0;
        for (CartItem item : getCartItems()) {
            total = total + (item.getProduct().getPrice() * item.getQuantity());
        }
        return total;
    }

    // Simple loop to check if a product is already sitting in the cart.
    private CartItem findCartItemByProductId(Long productId) {
        for (CartItem item : cartItemRepo.findAll()) {
            if (item.getProduct().getId().equals(productId)) {
                return item;
            }
        }
        return null;
    }

    public List<CartItem> removeFormCart(Long cartItemId) {
        CartItem item = getCartItemById(cartItemId);
        cartItemRepo.delete(item);
        return getCartItems();
    }

    private CartItem getCartItemById(Long cartItemId) {
        return cartItemRepo.findById(cartItemId)
                .orElseThrow(() -> new ResourceNotFoundException("Cart item not found with id: " + cartItemId));
    }
}
