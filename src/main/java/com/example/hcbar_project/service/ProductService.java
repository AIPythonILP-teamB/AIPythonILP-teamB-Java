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

    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Product not found with id: " + id));
    }

    public Optional<Product> findByJanCode(String janCode) {
        return productRepository.findByJanCode(janCode);
    }

    public List<Product> getAllProducts() {
        return productRepository.findByIsDeletedFalseOrderByIdAsc();
    }

    public Optional<Product> getProductById(Long id) {
        return productRepository.findById(id);
    }

    public Product saveProduct(Product product) {
        product.setIsDeleted(false);
        return productRepository.save(product);
    }

    public void deleteProduct(Long id) {
        productRepository.findById(id).ifPresent(product -> {
            product.setIsDeleted(true);
            productRepository.save(product);
        });
    }
}
