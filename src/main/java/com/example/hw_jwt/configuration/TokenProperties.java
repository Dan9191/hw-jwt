package com.example.hw_jwt.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Свойства приложения.
 */
@Component
@ConfigurationProperties(prefix = "token.properties")
@Data
public class TokenProperties {

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

    /**
     * Включить ли обновление конфигурации JWT в зависимости от настроек приложение.
     */
    private boolean configurationChangeSign = false;
}
