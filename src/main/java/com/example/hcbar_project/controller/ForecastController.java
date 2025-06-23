package com.example.hcbar_project.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.beans.factory.annotation.Value;

import com.example.hcbar_project.service.ProductService;
import com.example.hcbar_project.dto.ForecastResultDto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;

import java.time.LocalDate;
import java.util.*;

@Controller
public class ForecastController {

    @Autowired
    private ProductService productService;

    @Value("${azure.forecast.url}")
    private String forecastUrl;

    @Value("${azure.forecast.key}")
    private String forecastKey;

    @GetMapping("/forecast")
    public String showForecast(Model model) {
        model.addAttribute("activePage", "forecast");
        return "forecast";
    }

    @GetMapping("/api/forecast/sum")
    @ResponseBody
    public List<ForecastResultDto> getForecastSum(
            @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate selectedDate) {

        String apiUrl = forecastUrl + "?code=" + forecastKey;
        
        RestTemplate restTemplate = new RestTemplate();
        String response = restTemplate.getForObject(apiUrl, String.class);

        List<ForecastResultDto> result = new ArrayList<>();
        try {
            ObjectMapper mapper = new ObjectMapper();
            JsonNode root = mapper.readTree(response);

            int dayOfWeek = selectedDate.getDayOfWeek().getValue(); // 月=1, 木=4
            int daysToUse = (dayOfWeek == 1) ? 6 : (dayOfWeek == 4) ? 3 : 0;
            if (daysToUse == 0)
                return result;

            LocalDate start = selectedDate.plusDays(1);
            LocalDate end = start.plusDays(daysToUse - 1);

            Map<String, Double> sumByType = new HashMap<>();

            for (JsonNode node : root) {
                LocalDate forecastDate = LocalDate.parse(node.get("date").asText());
                if (!forecastDate.isBefore(start) && !forecastDate.isAfter(end)) {
                    JsonNode prediction = node.get("prediction");
                    prediction.fieldNames().forEachRemaining(type -> {
                        if (!type.equals("date") && !type.equals("total_prediction")) {
                            double val = prediction.get(type).asDouble();
                            sumByType.merge(type, val, Double::sum);
                        }
                    });
                }
            }

            for (Map.Entry<String, Double> entry : sumByType.entrySet()) {
                String code = mapBeerTypeToJan(entry.getKey());
                productService.findByJanCode(code).ifPresent(product -> result
                        .add(new ForecastResultDto(product.getProductName(), (int) Math.round(entry.getValue()))));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }

        return result;
    }

    private String mapBeerTypeToJan(String beerType) {
        switch (beerType) {
            case "white_beer":
                return "4901234567894";
            case "lager":
                return "4512345678907";
            case "pale_ale":
                return "4987654321097";
            case "fruit_beer":
                return "4545678901234";
            case "dark_beer":
                return "4999999999996";
            case "ipa":
                return "4571234567892";
            default:
                return "";
        }
    }
}