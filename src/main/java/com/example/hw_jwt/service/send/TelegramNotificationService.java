package com.example.hw_jwt.service.send;

import com.example.hw_jwt.configuration.AppTelegramProperties;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

/**
 * Сервис отправки сообщений в телеграм.
 */
@Service
@Slf4j
@RequiredArgsConstructor
public class TelegramNotificationService {

    private final AppTelegramProperties appTelegramProperties;

    public void sendCode(String destination, String code) {
        String message = String.format("Your confirmation code is: %s", code);
        String url = String.format("%s?chat_id=%s&text=%s",
                "https://api.telegram.org/bot" + appTelegramProperties.getToken() + "/sendMessage",
                destination,
                urlEncode(message));

        sendTelegramRequest(url);
    }

    private static void sendTelegramRequest(String url) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            try (CloseableHttpResponse response = httpClient.execute(request)) {
                int statusCode = response.getStatusLine().getStatusCode();
                if (statusCode != 200) {
                    log.error("Telegram API error. Status code: {}", statusCode);
                } else {
                    System.out.println("telegram response: " + response.getEntity());
                    log.info("Telegram message sent successfully");
                }
            }
        } catch (IOException e) {
            log.error("failed to send telegram response: " + e);
        }
    }
    private static String urlEncode(String value) {
        return URLEncoder.encode(value, StandardCharsets.UTF_8);
    }

}
