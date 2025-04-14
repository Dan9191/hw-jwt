package com.example.hw_jwt.service.send;

import com.example.hw_jwt.configuration.AppMailProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

/**
 * Сервис отправки электорнных сообщений.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class EmailNotificationService {

    private final Session mailSession;

    private final AppMailProperties appMailProperties;

    public void sendCode(String toEmail, String code) {
        try {
            Message message = new MimeMessage(mailSession);
            message.setFrom(new InternetAddress(appMailProperties.getFrom()));
            message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(toEmail));
            message.setSubject("Your OTP Code");
            message.setText("Your verification code is: " + code);
            Transport.send(message);
        } catch (MessagingException e) {
            log.error("Failed to send email");
            throw new RuntimeException("Failed to send email", e);
        }
    }
}
