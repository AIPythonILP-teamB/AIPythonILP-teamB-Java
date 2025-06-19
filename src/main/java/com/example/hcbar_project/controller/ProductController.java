package com.example.hcbar_project.controller;

import com.example.hcbar_project.model.Product;
import com.example.hcbar_project.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class ProductController {

    @Autowired
    private ProductService productService;

    @GetMapping("/product_list")
    public String showProductList(Model model) {
        model.addAttribute("productList", productService.getAllProducts());
        model.addAttribute("activePage", "product_list");
        return "product_list"; // resources/templates/product_list.html
    }

    @GetMapping("/product/add")
    public String showAddForm(Model model) {
        model.addAttribute("product", new Product());
        return "product_form"; // resources/templates/product_form.html
    }

    @GetMapping("/product/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Product product = productService.getProductById(id).orElse(null);
        if (product == null) {
            return "redirect:/product_list";
        }
        model.addAttribute("product", product);
        return "product_form";
    }

    @PostMapping("/product/save")
    public String saveProduct(@ModelAttribute Product product) {
        productService.saveProduct(product);
        return "redirect:/product_list";
    }
}
