package com.example.hcbar_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class CalendarController {
    @GetMapping("/calendar")
    public String showCalendarPage(Model model) {
        model.addAttribute("activePage", "calendar");
        return "calendar"; // 過去実績画面（calendar.html など）
    }

}