package com.example.hw_jwt.configuration;

import lombok.RequiredArgsConstructor;
import org.smpp.TCPIPConnection;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import java.util.Properties;

@Configuration
@RequiredArgsConstructor
public class AppConfig {

    private final AppMailProperties appMailProperties;

    private final AppSmppProperties appSmppProperties;

    @Bean("mailProperties")
    public Properties mailProperties() {
        Properties prop = new Properties();
        prop.put("mail.smtp.auth", appMailProperties.isSmtpAuthSign());
        prop.put("mail.smtp.ssl.enable", appMailProperties.isSmtpSslEnable());
        prop.put("mail.smtp.host", appMailProperties.getHost());
        prop.put("mail.smtp.port", appMailProperties.getSmtpPort());
        return prop;
    }

    @Bean
    public Session mailSession(Properties mailProperties) {
        return Session.getInstance(mailProperties, new Authenticator() {
            @Override
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(appMailProperties.getFrom(), appMailProperties.getPassword());
            }
        });
    }

    @Bean
    public org.smpp.Session smppSession() {
        org.smpp.Connection connection = new TCPIPConnection(appSmppProperties.getHost(), appSmppProperties.getPort());;
        return new org.smpp.Session(connection);
    }

}
