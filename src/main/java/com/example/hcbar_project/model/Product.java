package com.example.hcbar_project.model;

import jakarta.persistence.*;

@Entity
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "product_name")
    private String productName;

    private Integer price;

    @Column(name = "jan_code", unique = true)
    private String janCode;

    @Column(name = "is_deleted")
    private Boolean isDeleted = false;


    public void setIsDeleted(Boolean isDeleted) {
        this.isDeleted = isDeleted;
    }
    
    //setter
    public void setId(Long id) {
        this.id = id;
    }

    public void setProductName(String productName) {
        this.productName = productName;
    }

    public void setPrice(Integer price) {
        this.price = price;
    }

    public void setJanCode(String janCode) {
        this.janCode = janCode;
    }

    //getter
    public Long getId() {
        return id;
    }
    
    public String getProductName() {
        return productName;
    }

    public Integer getPrice() {
        return price;
    }

    public String getJanCode() {
        return janCode;
    }
}