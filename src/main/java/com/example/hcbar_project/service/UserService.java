package com.example.hcbar_project.service;

import com.example.hcbar_project.model.User;
import com.example.hcbar_project.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    public void registerUser(String userName, String email, String rawPassword) {
        if (userRepository.existsByEmail(email)) {
            throw new RuntimeException("そのメールアドレスはすでに使用されています");
        }

        User user = new User();
        user.setUserName(userName);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        user.setRole("USER"); // 管理者が作るユーザーは通常ユーザー
        user.setActive(true);

        userRepository.save(user);
    }
}
