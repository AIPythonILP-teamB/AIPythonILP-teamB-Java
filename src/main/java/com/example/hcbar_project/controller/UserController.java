package com.example.hcbar_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class UserController {

    // ユーザー一覧の表示
    // ユーザー情報の編集

    @GetMapping("/user_manage")
    public String showUserManagementPage(Model model) {
        model.addAttribute("activePage", "user_manage");
        return "user_manage"; // user_manage.html
    }
}