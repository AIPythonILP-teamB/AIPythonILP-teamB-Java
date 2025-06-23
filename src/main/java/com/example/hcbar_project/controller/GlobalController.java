package com.example.hcbar_project.controller;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;

import com.example.hcbar_project.config.CustomUserDetails;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.ui.Model;

@ControllerAdvice
public class GlobalController {

    @ModelAttribute
    public void addUserName(Model model,
            @AuthenticationPrincipal CustomUserDetails userDetails) {
        if (userDetails != null) {
            model.addAttribute("userName", userDetails.getUserName());
        }
    }
}
