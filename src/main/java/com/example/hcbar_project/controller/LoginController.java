package com.example.hcbar_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.core.Authentication;

@Controller
public class LoginController {

    // ログインフォームの表示
    @GetMapping("/login")
    public String login(@RequestParam(value = "error", required = false) String error,
            @RequestParam(value = "logout", required = false) String logout,
            Model model) {
        if (error != null) {
            model.addAttribute("errorMessage", "Emailまたはパスワードが間違っています");
        }
        if (logout != null) {
            model.addAttribute("successMessage", "ログアウトしました");
        }
        return "login";
    }

    // ログイン分岐
    @GetMapping("/default")
    public String redirectAfterLogin(Authentication authentication) {
        if (authentication.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/admin/home";
        } else {
            return "redirect:/user/home";
        }
    }

    // 管理者ホーム画面
    @GetMapping("/admin/home")
    public String adminHome(Model model) {
        return "admin";
    }
}
