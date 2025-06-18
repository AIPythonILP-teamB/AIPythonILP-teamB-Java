package com.example.hcbar_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class SaleController {

    // 売上入力フォーム表示
    // 売上データ登録
    // 売上一覧
    // 売上データの編集・削除

    @GetMapping("/admin_sale_input")
    public String showAdminSaleInput(Model model) {
        model.addAttribute("activePage", "admin_sale_input");
        return "admin_sale_input"; // admin_sale_input.html
    }
}