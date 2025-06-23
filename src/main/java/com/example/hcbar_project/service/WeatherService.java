package com.example.hcbar_project.service;

import java.time.LocalDate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.beans.factory.annotation.Value;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import com.example.hcbar_project.model.*;
import com.example.hcbar_project.repository.*;

@Service
public class WeatherService {

    @Autowired
    private WeatherRepository weatherRepository;

    @Value("${azure.function.url}")
    private String functionUrl;

    @Value("${azure.function.key}")
    private String functionKey;

    public void fetchAndSaveWeather(LocalDate date) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String fullUrl = functionUrl + "?code=" + functionKey;
            String json = restTemplate.getForObject(fullUrl, String.class);

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
