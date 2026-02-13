package com.example.mcpserverimpl;

import com.example.mcpserverimpl.config.WeatherProperties;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.ConfigurationPropertiesScan;

@SpringBootApplication
@ConfigurationPropertiesScan(basePackageClasses = WeatherProperties.class)
public class McpServerImplApplication {

    public static void main(String[] args) {
        SpringApplication.run(McpServerImplApplication.class, args);
    }
}
