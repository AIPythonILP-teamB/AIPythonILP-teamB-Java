package com.handcbar.myproject.repository;

import com.handcbar.myproject.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByJanCode(String janCode);
}