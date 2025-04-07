package com.example.hw_jwt.configuration;

import lombok.Getter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Свойства приложения.
 */
@Component
@ConfigurationProperties(prefix = "short-links.properties")
@Getter
public class AppProperties {
}
