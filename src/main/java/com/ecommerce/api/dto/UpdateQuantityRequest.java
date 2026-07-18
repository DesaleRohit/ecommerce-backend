package com.ecommerce.api.dto;

import jakarta.validation.constraints.Min;

public class UpdateQuantityRequest {

    @Min(value = 1, message = "quantity must be at least 1")
    private int quantity;

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}
