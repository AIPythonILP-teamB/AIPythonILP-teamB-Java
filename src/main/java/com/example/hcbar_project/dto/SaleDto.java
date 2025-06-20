package com.example.hcbar_project.dto;

import lombok.Data;

public class SaleDto {

    private Long productId;
    private Integer quantity;

    // --- Getter ---
    public Long getProductId() {
        return productId;
    }

    public Integer getQuantity() {
        return quantity;
    }

    // --- Setter ---
    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}