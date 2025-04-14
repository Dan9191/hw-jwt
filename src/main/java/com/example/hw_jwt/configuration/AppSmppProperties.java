package com.example.hw_jwt.configuration;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

/**
 * Свойства приложения.
 */
@Component
@ConfigurationProperties(prefix = "smpp.properties")
@Data
public class AppSmppProperties {
    private String systemId = "smppclient1";
    private String password = "password";
    private String host = "localhost";
    private int port = 2775;
    private String systemType = "OTP";
    private String sourceAddr = "OTPService";
}
