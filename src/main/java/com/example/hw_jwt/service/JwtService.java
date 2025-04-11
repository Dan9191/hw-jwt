package com.example.hw_jwt.service;

import com.example.hw_jwt.entity.JwtConfig;
import com.example.hw_jwt.entity.UserJwt;
import com.example.hw_jwt.repository.JwtConfigRepository;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Сервис для работы с токенами.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final JwtConfigRepository jwtConfigRepository;

    /**
     * Генерация токена.
     * @param userJwt
     * @return
     */
    public String generateToken(UserJwt userJwt) {

        JwtConfig config = jwtConfigRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("JWT configuration not found"));

        // Данные токена
        Map<String, Object> claims = new HashMap<>();
        claims.put("id", userJwt.getId());
        claims.put("login", userJwt.getLogin());
        claims.put("role", userJwt.getRole().getName());

        SecretKey secretKey = new SecretKeySpec(
                config.getKey().getBytes(StandardCharsets.UTF_8),
                config.getAlgorithm()
        );

        return Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + config.getExpMillis()))
                .signWith(SignatureAlgorithm.forName(config.getAlgorithm()), secretKey)
                .compact();
    }

}
