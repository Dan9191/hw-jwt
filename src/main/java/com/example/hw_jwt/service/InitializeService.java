package com.example.hw_jwt.service;

import com.example.hw_jwt.configuration.TokenProperties;
import com.example.hw_jwt.entity.JwtConfig;
import com.example.hw_jwt.repository.JwtConfigRepository;
import jakarta.annotation.PostConstruct;
import jakarta.persistence.EntityManager;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Optional;

/**
 * Сервис стартовой настройки.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class InitializeService {

    private final JwtConfigRepository jwtConfigRepository;
    private final TokenProperties tokenProperties;
    private final EntityManager entityManager;

    /**
     * Конфигурации генерации токена.
     */
    @PostConstruct
    public void initJwtConfig() {

        Optional<JwtConfig> config = jwtConfigRepository.findAll().stream().findFirst();
        if (tokenProperties.isConfigurationChangeSign() || config.isEmpty()) {
            // Очищаем таблицу
            jwtConfigRepository.deleteAll();

            // Создаем новую конфигурацию
            JwtConfig newConfig = new JwtConfig();
            newConfig.setAlgorithm(tokenProperties.getAlgorithm());
            newConfig.setKey(tokenProperties.getSecretKey());
            newConfig.setExpMillis(tokenProperties.getExpMillis());

            jwtConfigRepository.save(newConfig);
            log.info("Token configuration has been updated");
        }
        log.info("Token configuration was not updated");
    }
}
