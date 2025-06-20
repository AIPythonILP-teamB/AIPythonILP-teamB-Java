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

    /*一覧取得 */
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
}

