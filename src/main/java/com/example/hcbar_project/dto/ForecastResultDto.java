package com.example.hcbar_project.dto;

public class ForecastResultDto {
    private String productName;
    private int total;

    public ForecastResultDto(String productName, int total) {
        this.productName = productName;
        this.total = total;
    }

    public String getProductName() {
        return productName;
    }

    public int getTotal() {
        return total;
    }
}
