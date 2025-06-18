package com.example.hcbar_project.service;

import com.example.hcbar_project.model.Product;
import com.example.hcbar_project.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    @Autowired
    private ProductRepository productRepository;

    public List<Product> getAllProducts() {
        return productRepository.findByIsDeletedFalse();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public void saveProduct(Product product) {
        product.setIsDeleted(false);  // 常にfalseで登録
        productRepository.save(product);
    }
}