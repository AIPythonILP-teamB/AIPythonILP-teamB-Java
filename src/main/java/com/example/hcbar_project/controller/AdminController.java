package com.example.hcbar_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AdminController {
    // DBなしのrun用のテスト記述 けして～ by山本
    @GetMapping("/admin")
    public String adminHome(Model model) {
        // ここでログイン中のユーザー名を仮で設定（後ほどログイン連携へ）
        String adminName = "佐藤"; // 本来は認証済ユーザーの名前を取得する
        model.addAttribute("name", adminName);
        return "admin"; // templates/admin.html を返す
    }
}
