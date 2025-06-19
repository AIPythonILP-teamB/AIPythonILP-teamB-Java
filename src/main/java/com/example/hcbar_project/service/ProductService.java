package com.example.hcbar_project.service;

import com.example.hcbar_project.model.Product;
import com.example.hcbar_project.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    // 論理削除されていない商品を全件取得
    public List<Product> listAll() {
        return productRepository.findByIsDeletedFalse();
    }

    public Product findById(Long id) {
        return productRepository.findById(id).orElseThrow();
    }

}
