package com.example.hcbar_project.controller;

import java.time.LocalDate;
import java.util.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import com.example.hcbar_project.dto.SaleDto;
import com.example.hcbar_project.model.Sale;
import com.example.hcbar_project.service.*;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.fasterxml.jackson.core.type.TypeReference;

@Controller
@RequestMapping("/admin/sale")
@SessionAttributes({ "adminSales", "adminSaleDate" })
public class AdminSaleController {

    @Autowired
    private SaleService saleService;
    @Autowired
    private ProductService productService;
    @Autowired
    private WeatherService weatherService;

    /** ① 入力画面表示 */
    @GetMapping("/input")
    public String showInput(Model model) {
        model.addAttribute("adminSaleDate", LocalDate.now());
        model.addAttribute("adminSales", new ArrayList<Sale>());
        model.addAttribute("products", productService.getAllProducts());
        model.addAttribute("activePage", "admin_sale_input");
        return "admin_sale_input";

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

            model.addAttribute("adminSales", sales);
            model.addAttribute("adminSaleDate", date);
            model.addAttribute("sales", sales);
            model.addAttribute("saleDate", date);
            model.addAttribute("activePage", "admin_sale_confirm");
            return "admin_sale_confirm";

        } catch (Exception e) {
            e.printStackTrace();
            return "error";
        }
    }

    /** ③ 登録実行 */
    @PostMapping("/save")
    public String save(@ModelAttribute("adminSales") List<Sale> sales,
            @ModelAttribute("adminSaleDate") LocalDate date,
            Model model) {

        for (Sale s : sales) {
            s.setSaleDate(date);
        }
        saleService.registerAll(sales);

        // ここで天気情報取得＆DB保存（裏処理）
        weatherService.fetchAndSaveWeather(date);

        model.addAttribute("adminSales", new ArrayList<>());
        model.addAttribute("activePage", "admin_sale_input");
        return "redirect:/admin/sale/input?completed=true";
    }
}
