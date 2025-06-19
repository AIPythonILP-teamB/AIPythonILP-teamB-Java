package com.example.hcbar_project.repository;

import com.example.hcbar_project.model.Weather;

import java.time.LocalDate;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface WeatherRepository extends JpaRepository<Weather, Long> {
    // 日付で天気を取得
    Optional<Weather> findByDate(LocalDate date);
}