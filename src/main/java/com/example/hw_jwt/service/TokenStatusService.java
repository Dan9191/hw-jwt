package com.example.hw_jwt.service;

import com.example.hw_jwt.entity.TokenStatus;
import com.example.hw_jwt.repository.TokenStatusRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * Сервис, кеширующий данные по статусам.
 */
@Service
@RequiredArgsConstructor
public class TokenStatusService {

    private final TokenStatusRepository tokenStatusRepository;

    private Map<Integer, TokenStatus> statuses;

    @PostConstruct
    void init() {
        statuses = tokenStatusRepository.findAll().stream().collect(Collectors.toMap(TokenStatus::getId, Function.identity()));
    }

    public TokenStatus findById(int id) {
        return statuses.get(id);
    }

}
