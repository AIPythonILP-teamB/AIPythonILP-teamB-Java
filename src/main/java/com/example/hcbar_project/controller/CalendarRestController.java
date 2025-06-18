package com.example.hcbar_project.controller;

import com.example.hcbar_project.model.*;
import com.example.hcbar_project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/api/calendar")
public class CalendarRestController {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private WeatherRepository weatherRepository;

    @Autowired
    private ProductRepository productRepository;

    // ① カレンダーに表示するイベント一覧（1ヶ月分など）
    @GetMapping("/events")
    public List<Map<String, Object>> getCalendarEvents(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate start,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate end) {

        // 該当範囲の販売データと天気を取得
        List<Weather> weathers = weatherRepository.findByDateBetween(start, end);
        List<Sale> sales = saleRepository.findBySaleDateBetween(start, end);

        // 日付ごとの販売本数を集計
        Map<LocalDate, Integer> salesPerDate = sales.stream()
                .collect(Collectors.groupingBy(
                        Sale::getSaleDate,
                        Collectors.summingInt(Sale::getQuantity)));

        // 日付ごとにイベントを作成
        List<Map<String, Object>> result = new ArrayList<>();
        for (Weather w : weathers) {
            LocalDate date = w.getDate();
            int totalSales = salesPerDate.getOrDefault(date, 0);
            String title = w.getWeatherMain() + "\n" + totalSales + "本";
            result.add(Map.of(
                    "title", title,
                    "start", date.toString()));
        }

        return result;
    }

    // ② 日付クリック時の詳細モーダル表示
    @GetMapping("/detail")
    public Map<String, Object> getDetailByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        Weather w = weatherRepository.findByDate(date);
        List<Sale> sales = saleRepository.findBySaleDate(date);

        int total = sales.stream().mapToInt(Sale::getQuantity).sum();

        List<Map<String, Object>> productSales = sales.stream()
                .collect(Collectors.groupingBy(
                        s -> s.getProduct().getProductName(),
                        Collectors.summingInt(Sale::getQuantity)))
                .entrySet()
                .stream()
                .map(entry -> {
                    Map<String, Object> m = new HashMap<>();
                    m.put("name", entry.getKey());
                    m.put("quantity", entry.getValue());
                    return m;
                })

                .collect(Collectors.toList());

        return Map.of(
                "date", date.toString(),
                "weatherMain", w.getWeatherMain(),
                "maxTemp", w.getMaxTemp(),
                "minTemp", w.getMinTemp(),
                "windSpeed", w.getWindSpeed(),
                "totalSales", total,
                "productSales", productSales);
    }
}
