package com.example.hcbar_project.controller;

import com.example.hcbar_project.model.User;
import com.example.hcbar_project.service.UserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserRestController {

    @Autowired
    private UserService userService;

    /* 一覧取得 */
    @GetMapping
    public List<User> getAllUsers() {
        return userService.getAllUsers();
    }

    /* 新規作成 */
    @PostMapping
    public User createUser(@RequestBody User user) {
        return userService.saveUser(user);
    }

    /* 更新 */
    @PutMapping("/{id}")
    public User updateUser(@PathVariable Long id, @RequestBody User user) {
        user.setId(id);
        return userService.saveUser(user);
    }

    /* 論理削除 */
    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Long id) {
        userService.deleteUser(id);
    }

    // パスワード再設定
    @PostMapping("/{id}/reset-password")
    public String resetPassword(@PathVariable Long id, @RequestBody PasswordDto dto) {
        return userService.getUserById(id)
                .map(user -> {
                    user.setPassword(dto.getPassword()); // パスワード（生）を一旦セット
                    userService.saveUser(user); // saveUser内でハッシュ化される
                    return "Password updated";
                })
                .orElse("User not found");
    }

    // パスワードDTO（内部クラス）
    public static class PasswordDto {
        private String password;

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }
    }
}