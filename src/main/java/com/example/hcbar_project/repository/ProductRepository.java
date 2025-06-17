package com.example.hcbar_project.repository;

import com.example.hcbar_project.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByJanCode(String janCode);
}