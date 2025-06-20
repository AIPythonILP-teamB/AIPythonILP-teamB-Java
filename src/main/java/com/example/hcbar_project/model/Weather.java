package com.example.hcbar_project.model;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Weather {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private LocalDate date;

    @Column(name = "weather_main")
    private String weatherMain;

    @Column(name = "max_temp")
    private Float maxTemp;

    @Column(name = "min_temp")
    private Float minTemp;

    @Column(name = "wind_speed")
    private Float windSpeed;

    @Column(name = "icon")
    private String icon;

    // --- Getter ---
    public Long getId() {
        return id;
    }

    public LocalDate getDate() {
        return date;
    }

    public String getWeatherMain() {
        return weatherMain;
    }

    public float getMaxTemp() {
        return maxTemp;
    }

    public float getMinTemp() {
        return minTemp;
    }

    public float getWindSpeed() {
        return windSpeed;
    }

    public String getIcon() {
        return icon;
    }

    // --- Setter ---
    public void setId(Long id) {
        this.id = id;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public void setWeatherMain(String weatherMain) {
        this.weatherMain = weatherMain;
    }

    public void setMaxTemp(float maxTemp) {
        this.maxTemp = maxTemp;
    }

    public void setMinTemp(float minTemp) {
        this.minTemp = minTemp;
    }

    public void setWindSpeed(float windSpeed) {
        this.windSpeed = windSpeed;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
