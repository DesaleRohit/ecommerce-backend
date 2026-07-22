package com.ecommerce.api.controller;

import com.ecommerce.api.dto.UpdateStatusRequest;
import com.ecommerce.api.entity.Order;
import com.ecommerce.api.entity.OrderItem;
import com.ecommerce.api.service.OrderService;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/orders")
public class OrderController {

    private OrderService orderService;
    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @PostMapping("/checkout")
    public ResponseEntity<Map<String, Object>> chekout() {
        Order order = orderService.checkout();
        return ResponseEntity.status(HttpStatus.CREATED).body(buildOrderResponse(order));
    }

    @GetMapping
    public List<Order> getAllOrders() {
        return orderService.getAllOrders();
    }

    @GetMapping("/{id}")
    public Map<String, Object> getOrderById(@PathVariable Long id) {
        Order order = orderService.getOrderById(id);
        return buildOrderResponse(order);
    }

    @PatchMapping("/{id}/status")
    public Order updateStatus(@PathVariable Long id, @Valid @RequestBody UpdateStatusRequest request) {
        return orderService.updateStatus(id, request.getStatus());
    }

    private Map<String, Object> buildOrderResponse(Order order) {
        List<OrderItem> items = orderService.getOrderItems(order.getId());
        Map<String, Object> response = new HashMap<>();
        response.put("order", order);
        response.put("items", items);
        return response;
    }
}
