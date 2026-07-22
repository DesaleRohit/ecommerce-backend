package com.ecommerce.api.service;

import com.ecommerce.api.entity.Products;
import com.ecommerce.api.entity.Order;
import com.ecommerce.api.entity.OrderItem;
import com.ecommerce.api.entity.CartItem;
import com.ecommerce.api.entity.OrderStatus;
import com.ecommerce.api.exception.InsufficientStockException;
import com.ecommerce.api.exception.ResourceNotFoundException;
import com.ecommerce.api.repository.OrderItemRepo;
import com.ecommerce.api.repository.OrderRepo;
import com.ecommerce.api.repository.ProductRepo;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class OrderService {

    private OrderRepo orderRepo;

    private OrderItemRepo orderItemRepo;

    private ProductRepo productRepo;

    private CartService cartService;

    public OrderService(OrderRepo orderRepo, OrderItemRepo orderItemRepo, ProductRepo productRepo, CartService cartService) {
        this.orderRepo = orderRepo;
        this.orderItemRepo = orderItemRepo;
        this.productRepo = productRepo;
        this.cartService = cartService;
    }

    public Order checkout() {
        List<CartItem> cartItems = cartService.getCartItems();

        if (cartItems.isEmpty()) {
            throw new IllegalStateException("Cannot checkout - cart is empty");
        }

        for (CartItem cartItem : cartItems) {
            Products product = cartItem.getProduct();
            if (cartItem.getQuantity() > product.getStockQuantity()) {
                throw new InsufficientStockException(
                        "Not enough stock for " + product.getName() + ". Available: " + product.getStockQuantity());
            }
        }

        Order order = new Order();
        order.setStatus(OrderStatus.PENDING);
        order.setCreatedAt(LocalDateTime.now());
        order.setTotalPrice(cartService.calculateCartTotal());
        order = orderRepo.save(order);

        for (CartItem cartItem : cartItems) {
            Products product = cartItem.getProduct();

            product.setStockQuantity(product.getStockQuantity() - cartItem.getQuantity());
            productRepo.save(product);

            OrderItem orderItem = new OrderItem(order, product, cartItem.getQuantity(), product.getPrice());
            orderItemRepo.save(orderItem);
        }

        cartService.clearCart();

        return order;
    }

    public List<Order> getAllOrders() {
        return orderRepo.findAll();
    }

    public List<OrderItem> getOrderItems(Long orderId) {
        return orderItemRepo.findOrderById(orderId);
    }


    public Order getOrderById(Long id) {
        return orderRepo.findById(id)
                .orElseThrow(()-> new ResourceNotFoundException("Order not found with id: " + id));
    }

    public Order updateStatus(Long id, OrderStatus newStatus) {
        Order order = getOrderById(id);
        order.setStatus(newStatus);
        return orderRepo.save(order);
    }
}
