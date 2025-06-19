package com.example.hcbar_project.controller;

import com.example.hcbar_project.model.User;
import com.example.hcbar_project.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    /** ユーザー一覧表示 */
    @GetMapping("/user_manage")
    public String showUserList(Model model) {
        // DBから全ユーザーを取得してテンプレートへ渡す
        model.addAttribute("userManage", userService.getAllUsers());
        model.addAttribute("activePage", "user_manage");
        return "user_manage";  // resources/templates/user_manage.html
    }

    /** 新規ユーザー追加フォーム表示 */
    @GetMapping("/user/add")
    public String showAddForm(Model model) {
        // 空のUserオブジェクトを渡し、フォームにバインド
        model.addAttribute("user", new User());
        return "user_manage";  // resources/templates/user_manage.html
    }

    /** 既存ユーザー編集フォーム表示 */
    @GetMapping("/user/edit/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        // IDでユーザーを取得。なければ一覧へ戻す
        User user = userService.getUserById(id).orElse(null);
        if (user == null) {
            return "redirect:/user_manage";
        }
        model.addAttribute("user", user);
        return "user_manage";
    }

    /** 新規／更新の保存処理 */
    @PostMapping("/user/save")
    public String saveUser(@ModelAttribute User user) {
        // Service側でINSERT or UPDATEを使い分け
        userService.saveUser(user);
        return "redirect:/user_manage";
    }
}
