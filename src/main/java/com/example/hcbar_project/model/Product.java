package com.example.hcbar_project.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
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
}