package com.example.hcbar_project.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.hcbar_project.model.*;
import com.example.hcbar_project.repository.*;

@Service
public class WeatherService {

    @Autowired
    private WeatherRepository weatherRepository;

    private static final String FUNCTION_URL = "https://teamb-func.azurewebsites.net/api/gettodayowm?code=hJ7GWeVHFWyHzfZo088GuGittzIXwPazHqmGQ__SjBWbAzFu5upddw==";

    public void fetchAndSaveWeather(LocalDate date) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String json = restTemplate.getForObject(FUNCTION_URL, String.class);

            ObjectMapper mapper = new ObjectMapper();
            JsonNode node = mapper.readTree(json);

            Weather weather = new Weather();
            weather.setDate(LocalDate.parse(node.get("date").asText()));
            weather.setWeatherMain(node.get("weatherMain").asText());
            weather.setMaxTemp((float) node.get("maxTemp").asDouble());
            weather.setMinTemp((float) node.get("minTemp").asDouble());
            weather.setWindSpeed((float) node.get("windSpeed").asDouble());
            weather.setIcon(node.get("icon").asText());

            // 日付で重複を避ける
            if (!weatherRepository.findByDate(weather.getDate()).isPresent()) {
                weatherRepository.save(weather);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
