package com.example.mcpserverimpl.service;

import com.example.mcpserverimpl.config.WeatherProperties;
import com.example.mcpserverimpl.model.CurrentWeather;
import com.example.mcpserverimpl.model.OpenMeteoResponse;
import com.example.mcpserverimpl.model.WeatherInfo;
import java.time.Instant;
import java.time.OffsetDateTime;
import java.time.format.DateTimeParseException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class WeatherService {

    private static final String LOCATION_NAME = "Abu Dhabi";
    private static final Logger logger = LoggerFactory.getLogger(WeatherService.class);

    private final RestTemplate restTemplate;
    private final WeatherProperties properties;

    public WeatherService(WeatherProperties properties) {
        this.restTemplate = new RestTemplate();
        this.properties = properties;
    }

    public WeatherInfo getCurrentWeather() {
        var uri = UriComponentsBuilder.fromHttpUrl(properties.getBaseUrl())
                .queryParam("latitude", properties.getLatitude())
                .queryParam("longitude", properties.getLongitude())
                .queryParam("current_weather", true)
                .build()
                .toUri();

        logger.debug("Requesting Open-Meteo current weather: url={}", uri);
        OpenMeteoResponse response = restTemplate.getForObject(uri, OpenMeteoResponse.class);
        if (response == null || response.getCurrentWeather() == null) {
            logger.error("Open-Meteo response missing current weather data");
            throw new IllegalStateException("No current weather data received from Open-Meteo");
        }

        CurrentWeather current = response.getCurrentWeather();
        logger.info("Open-Meteo current weather received: tempC={}, windKph={}, code={}",
                current.getTemperature(), current.getWindSpeed(), current.getWeatherCode());
        return new WeatherInfo(
                LOCATION_NAME,
                current.getTemperature(),
                current.getWindSpeed(),
                current.getWeatherCode(),
                parseInstant(current.getTime())
        );
    }

    private Instant parseInstant(String timeValue) {
        if (timeValue == null || timeValue.isBlank()) {
            logger.debug("Open-Meteo time missing; defaulting to now");
            return Instant.now();
        }
        try {
            return OffsetDateTime.parse(timeValue).toInstant();
        } catch (DateTimeParseException ex) {
            logger.warn("Open-Meteo time parse failed; defaulting to now: value={}", timeValue);
            return Instant.now();
        }
    }
}
