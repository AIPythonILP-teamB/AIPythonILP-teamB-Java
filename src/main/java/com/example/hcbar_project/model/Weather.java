package com.handcbar.myproject.model;

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
}