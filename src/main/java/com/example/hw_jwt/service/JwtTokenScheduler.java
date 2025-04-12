package com.example.hw_jwt.service;

import com.example.hw_jwt.repository.JwtTokenRepository;
import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * Сервис для перевода записей в "просроченные".
 */
@Component
@RequiredArgsConstructor
@Slf4j
public class JwtTokenScheduler {

    private final JwtTokenRepository jwtTokenRepository;


    @PostConstruct
    public void startScheduler() {
        ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
        scheduler.scheduleAtFixedRate(this::deactivateTokens, 0, 1, TimeUnit.MINUTES);
    }

    /**
     * Метод для перевода ссылок в неактивный режим.
     */
    @Transactional
    public void deactivateTokens() {
        try {
            log.info("Start deactivating tokens");
            jwtTokenRepository.markExpiredTokensAsExpired();
        } catch (Exception e) {
            log.error("Ошибка при выполнении deactivateTokens: ", e);
        }
    }

}
