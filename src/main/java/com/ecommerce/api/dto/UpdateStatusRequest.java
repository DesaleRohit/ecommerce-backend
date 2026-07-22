package com.ecommerce.api.dto;

import com.ecommerce.api.entity.OrderStatus;
import jakarta.validation.constraints.NotNull;

// The JSON shape for "change this order's status".
public class UpdateStatusRequest {

    @NotNull(message = "status is required")
    private OrderStatus status;

    public OrderStatus getStatus() {
        return status;
    }

    public void setStatus(OrderStatus status) {
        this.status = status;
    }
}
