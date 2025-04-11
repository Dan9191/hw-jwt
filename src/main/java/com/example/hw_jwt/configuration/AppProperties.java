package com.example.hw_jwt.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Свойства приложения.
 */
@Component
@ConfigurationProperties(prefix = "short-links.properties")
@Data
public class AppProperties {

    /**
     * Алгоритм кодирования токена.
     */
    private String algorithm;

    /**
     * Алгоритм кодирования токена.
     */
    private String secretKey;

    /**
     * Срок жизни токена в миллисекундах.
     */
    private long expMillis;
}
