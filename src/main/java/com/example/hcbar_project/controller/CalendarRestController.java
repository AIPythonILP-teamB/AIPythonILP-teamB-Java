package com.example.hcbar_project.controller;

import com.example.hcbar_project.model.*;
import com.example.hcbar_project.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
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

    // カレンダーに表示するイベント一覧
    @GetMapping("/events")
    public List<Map<String, Object>> getCalendarEvents(
            @RequestParam String start,
            @RequestParam String end) {

        // 文字列からLocalDateを抽出（先頭10文字だけ使う）
        LocalDate startDate = LocalDate.parse(start.substring(0, 10));
        LocalDate endDate = LocalDate.parse(end.substring(0, 10));

        // 該当範囲のデータ取得
        List<Weather> weathers = weatherRepository.findByDateBetween(startDate, endDate);
        List<Sale> sales = saleRepository.findBySaleDateBetween(startDate, endDate);

        // 日付ごとの販売本数を集計
        Map<LocalDate, Integer> salesPerDate = sales.stream()
                .collect(Collectors.groupingBy(
                        Sale::getSaleDate,
                        Collectors.summingInt(Sale::getQuantity)));

        // イベント生成
        List<Map<String, Object>> result = new ArrayList<>();
        for (Weather w : weathers) {
            LocalDate date = w.getDate();
            int totalSales = salesPerDate.getOrDefault(date, 0);
            result.add(Map.of(
                    "title", totalSales + "本",
                    "start", date.toString(),
                    "icon", w.getIcon()));
        }

        return result;
    }

    // 日付クリック時の詳細モーダル表示
    @GetMapping("/detail")
    public Map<String, Object> getDetailByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        Optional<Weather> optionalWeather = weatherRepository.findByDate(date);
        List<Sale> sales = saleRepository.findBySaleDate(date);

        // nullやデータなしの場合に備える
        if (optionalWeather.isEmpty() || sales == null || sales.isEmpty()) {
            return Map.of(); // 空のオブジェクトを返す
        }

        Weather w = optionalWeather.get(); // 必ず存在している前提で取り出す

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

    @GetMapping("/sales")
    public List<Map<String, Object>> getSalesByDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {

        List<Sale> sales = saleRepository.findBySaleDate(date);

        return sales.stream().map(s -> {
            Map<String, Object> m = new HashMap<>();
            m.put("id", s.getId());
            m.put("productName", s.getProduct().getProductName());
            m.put("productId", s.getProduct().getId());
            m.put("quantity", s.getQuantity());
            return m;
        }).collect(Collectors.toList());
    }

    @PutMapping("/sale/{id}")
    public ResponseEntity<?> updateSale(@PathVariable Long id, @RequestBody Map<String, Object> payload) {
        try {
            Sale sale = saleRepository.findById(id)
                    .orElseThrow(() -> new NoSuchElementException("Sale not found for ID: " + id));

            Object rawQuantity = payload.get("quantity");
            if (rawQuantity == null) {
                return ResponseEntity.badRequest().body("quantityが指定されていません");
            }

            int quantity = Integer.parseInt(rawQuantity.toString()); // ← 安全な変換
            sale.setQuantity(quantity);
            saleRepository.save(sale);

            return ResponseEntity.ok().build();
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("更新中にエラーが発生しました: " + e.getMessage());
        }

    }
}
