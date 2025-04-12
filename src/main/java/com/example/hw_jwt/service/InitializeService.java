package com.example.hw_jwt.service;

import com.example.hw_jwt.configuration.AppProperties;
import com.example.hw_jwt.entity.JwtConfig;
import com.example.hw_jwt.repository.JwtConfigRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

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

        Optional<JwtConfig> config = jwtConfigRepository.findAll().stream().findFirst();
        if (appProperties.isConfigurationChangeSign() || config.isEmpty()) {
            // Очищаем таблицу
            jwtConfigRepository.deleteAll();

            // Создаем новую конфигурацию
            JwtConfig newConfig = new JwtConfig();
            newConfig.setAlgorithm(appProperties.getAlgorithm());
            newConfig.setKey(appProperties.getSecretKey());
            newConfig.setExpMillis(appProperties.getExpMillis());

            jwtConfigRepository.save(newConfig);
        }


    }
}
