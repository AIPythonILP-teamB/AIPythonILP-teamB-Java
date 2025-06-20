package com.example.hcbar_project.dto;

import lombok.Data;

@Data
public class SaleDto {
    private Long productId;
    private String productName; 
    private Integer quantity;
}
