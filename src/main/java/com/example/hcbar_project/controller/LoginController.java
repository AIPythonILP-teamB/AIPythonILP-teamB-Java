package com.example.hcbar_project.controller;

import com.example.hcbar_project.model.User;
import com.example.hcbar_project.repository.UserRepository;
import com.example.hcbar_project.service.CustomUserDetailsService;
import com.example.hcbar_project.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.security.core.Authentication;


@Controller
public class LoginController {
    // ログインフォームの表示
    // ログイン認証(/login)
    // ログアウト処理(/logout)
    // 管理者がユーザー登録(/register)、パスワード再設定(/reset-password)

    

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
        // 例: 管理者用の統計データやユーザー一覧を表示したい場合
        // model.addAttribute("name", adminName);
        return "admin"; // admin.html を表示
    }

    // 例: 管理者が全ユーザーを確認するページ
    @Autowired
    private UserRepository userRepository;


    @GetMapping("/admin/signup")
    public String showSignupForm() {
        return "admin_signup"; // 管理者用のユーザー登録画面
    }

    @PostMapping("/admin/signup")
    public String registerUser(@RequestParam String userName,
                               @RequestParam String email,
                               @RequestParam String password,
                               @RequestParam String confirmPassword,
                               RedirectAttributes redirectAttributes,
                               UserService userService) {

        try {
            if (userName.trim().isEmpty() || email.trim().isEmpty() || password.trim().isEmpty()) {
                redirectAttributes.addFlashAttribute("errorMessage", "全ての項目を入力してください");
                return "redirect:/admin/signup";
            }

            if (!password.equals(confirmPassword)) {
                redirectAttributes.addFlashAttribute("errorMessage", "パスワードが一致しません");
                return "redirect:/admin/signup";
            }

            userService.registerUser(userName, email, password);
            redirectAttributes.addFlashAttribute("successMessage", "新しいユーザーが登録されました。");
            return "redirect:/admin/home";

        } catch (RuntimeException e) {
            redirectAttributes.addFlashAttribute("errorMessage", e.getMessage());
            return "redirect:/admin/signup";
        }
    }
}


