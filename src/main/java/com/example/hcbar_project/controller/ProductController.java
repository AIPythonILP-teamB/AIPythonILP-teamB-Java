package com.example.hcbar_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ProductController {

    // 商品一覧表示
    // 商品新規登録フォーム表示
    // 商品登録処理
    // 商品編集・削除

    @GetMapping("product_list")
    public String showProductList(Model model) {
        model.addAttribute("activePage", "product_list");
        return "product_list"; // product_list.html
    }
}
