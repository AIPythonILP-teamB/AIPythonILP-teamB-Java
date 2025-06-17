package com.handcbar.myproject.repository;

import com.handcbar.myproject.model.Weather;
import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
    // 日付で天気を取得
    Weather findByDate(java.time.LocalDate date);
}