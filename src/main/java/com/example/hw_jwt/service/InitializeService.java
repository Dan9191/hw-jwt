package com.example.hw_jwt.service;

import com.example.hw_jwt.configuration.AppProperties;
import com.example.hw_jwt.entity.JwtConfig;
import com.example.hw_jwt.repository.JwtConfigRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

/**
 * Сервис стартовой настройки.
 */
@Service
@RequiredArgsConstructor
public class InitializeService {

    private final JwtConfigRepository jwtConfigRepository;
    private final AppProperties appProperties;
    private final EntityManager entityManager;

    /**
     * Конфигурации генерации токена.
     */
    @PostConstruct
    public void initJwtConfig() {
        // Очищаем таблицу
        jwtConfigRepository.deleteAll();

        // Создаем новую конфигурацию
        JwtConfig config = new JwtConfig();
        config.setAlgorithm(appProperties.getAlgorithm());
        config.setKey(appProperties.getSecretKey());
        config.setExpMillis(appProperties.getExpMillis());

        jwtConfigRepository.save(config);
    }
}
