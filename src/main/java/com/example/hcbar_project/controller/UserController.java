package com.example.hcbar_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class UserController {

    //一般ユーザーの機能（例：マイページ、プロフィール変更、従業員向け処理）

    @GetMapping("/user_manage")
    public String showUserManagementPage(Model model) {
        model.addAttribute("activePage", "user_manage");
        return "user_manage"; // user_manage.html
    }

    @GetMapping("/home")
        public String userHome() {
        return "user"; // user.html
    }

}


