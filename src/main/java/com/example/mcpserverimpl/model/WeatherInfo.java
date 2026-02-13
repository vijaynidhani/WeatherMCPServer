package com.example.mcpserverimpl.model;

import java.time.Instant;

public record WeatherInfo(
        String location,
        double temperatureCelsius,
        double windSpeedKph,
        int weatherCode,
        Instant observedAt
) {
}
