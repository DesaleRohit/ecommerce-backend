package com.ecommerce.api.controller;

import com.ecommerce.api.entity.Order;
import com.ecommerce.api.entity.OrderItem;
import com.ecommerce.api.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    private Map<String, Object> buildOrderResponse(Order order) {
        List<OrderItem> items = orderService.getOrderItems(order.getId());
        Map<String, Object> response = new HashMap<>();
        response.put("order", order);
        response.put("items", items);
        return response;
    }
}
