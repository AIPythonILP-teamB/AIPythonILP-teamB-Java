package com.example.hcbar_project.repository;

import com.example.hcbar_project.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Product findByJanCode(String janCode);

    List<Product> findByIsDeletedFalseOrderByIdAsc();; // 論理削除されていない商品を取得

}