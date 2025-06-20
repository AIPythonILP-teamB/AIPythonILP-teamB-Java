package com.example.hcbar_project.dto;

public class SaleDto {

    private Long productId;
    private String productName; // JSから送られるキーと一致
    private Integer quantity;

    // --- Getter and Setter ---

    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public String getProductName() {
        return productName;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}