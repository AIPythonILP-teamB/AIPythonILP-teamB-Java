package com.example.hcbar_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class ForecastController {

    @GetMapping("/forecast")
    public String showForecast(Model model) {
        model.addAttribute("activePage", "forecast");
        return "forecast";
    }
}