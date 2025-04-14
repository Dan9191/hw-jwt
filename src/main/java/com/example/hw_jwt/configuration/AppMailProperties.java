package com.example.hw_jwt.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Свойства приложения.
 */
@Component
@ConfigurationProperties(prefix = "mail.properties")
@Data
public class AppMailProperties {
    private String from;
    private String password;
    private String host = "smtp.mail.ru";
    private String smtpPort = "465";
    private boolean smtpAuthSign = true;
    private boolean smtpSslEnable = true;
}
