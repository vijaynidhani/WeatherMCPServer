package com.example.mcpserverimpl.web;

import com.example.mcpserverimpl.model.WeatherInfo;
import com.example.mcpserverimpl.service.WeatherService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/weather")
public class WeatherController {

    private final WeatherService weatherService;

    public WeatherController(WeatherService weatherService) {
        this.weatherService = weatherService;
    }

    @GetMapping("/abu-dhabi")
    public WeatherInfo getAbuDhabiWeather() {
        return weatherService.getCurrentWeather();
    }
}
