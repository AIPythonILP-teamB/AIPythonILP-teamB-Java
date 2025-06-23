
// 一般ユーザ向けの販売実績入力画面：実装途中

package com.example.hcbar_project.controller;

import java.security.Principal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.hcbar_project.dto.SaleDto;
import com.example.hcbar_project.model.Sale;
import com.example.hcbar_project.model.User;
import com.example.hcbar_project.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;

@Controller
@RequestMapping("/sale")
@SessionAttributes({ "userSales", "userSaleDate" })
public class SaleController {

    @Autowired
    private SaleService saleService;

    @Autowired
    private ProductService productService;

    @Autowired
    private UserService userService;

    @Autowired
    private WeatherService weatherService;

    @GetMapping("/input")
    public String showInput(Model model) {
        model.addAttribute("userSaleDate", LocalDate.now());
        model.addAttribute("userSales", new ArrayList<Sale>());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("sale", new Sale());
        model.addAttribute("activePage", "sale_input");
        return "sale_input";
    }

    @PostMapping("/confirm")
    public String confirm(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam("salesJson") String salesJson,
            Model model) {
        try {
            ObjectMapper mapper = new ObjectMapper();
            List<SaleDto> saleDtos = mapper.readValue(
                    salesJson,
                    mapper.getTypeFactory().constructCollectionType(List.class, SaleDto.class));

            List<Sale> sales = new ArrayList<>();
            for (SaleDto dto : saleDtos) {
                Sale sale = new Sale();
                sale.setProduct(productService.findById(dto.getProductId()));
                sale.setQuantity(dto.getQuantity());
                sales.add(sale);
            }

            model.addAttribute("userSales", sales);
            model.addAttribute("userSaleDate", date);
            model.addAttribute("sales", sales);
            model.addAttribute("saleDate", date);
            model.addAttribute("activePage", "sale_confirm");
            return "sale_confirm";

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /* 保存実行 */
    @PostMapping("/save")
    public String save(
            @ModelAttribute("userSales") List<Sale> sales,
            @ModelAttribute("userSaleDate") LocalDate date,
            Principal principal,
            Model model) {

        String email = principal.getName();
        User user = userService.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("ユーザーが見つかりません"));

        for (Sale sale : sales) {
            sale.setSaleDate(date);
            sale.setUser(user);
        }
        saleService.registerAll(sales);

        // 天気情報の保存（一般ユーザー操作用）
        weatherService.fetchAndSaveWeather(date);

        model.addAttribute("userSales", new ArrayList<>());
        model.addAttribute("activePage", "sale_input");
        return "redirect:/sale/input?completed=true";
    }
}