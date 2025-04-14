package com.example.hw_jwt.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Свойства приложения.
 */
@Component
@ConfigurationProperties(prefix = "telegram.properties")
@Data
public class AppTelegramProperties {

    private String token;
}
