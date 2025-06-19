package com.example.hcbar_project.dto;

import lombok.Data;

@Data
public class SaleDto {
    private Long productId;
    private String productName; // ← 追加（JSから送信されるキーと一致）
    private Integer quantity;
}
