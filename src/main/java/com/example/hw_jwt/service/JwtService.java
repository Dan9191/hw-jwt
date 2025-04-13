package com.example.hw_jwt.service;

import com.example.hw_jwt.entity.JwtConfig;
import com.example.hw_jwt.entity.JwtToken;
import com.example.hw_jwt.entity.RoleType;
import com.example.hw_jwt.entity.TokenStatus;
import com.example.hw_jwt.entity.TokenStatusType;
import com.example.hw_jwt.entity.UserJwt;
import com.example.hw_jwt.model.TokenValidationResult;
import com.example.hw_jwt.repository.JwtConfigRepository;
import com.example.hw_jwt.repository.JwtTokenRepository;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.example.hw_jwt.entity.TokenStatusType.ACTIVE;
import static com.example.hw_jwt.entity.TokenStatusType.ACTIVE_STATUSES;

/**
 * Сервис для работы с токенами.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class JwtService {

    private final JwtConfigRepository jwtConfigRepository;

    private final JwtTokenRepository jwtTokenRepository;

    private final TokenStatusService tokenStatusService;

    /**
     * Генерация токена.
     * @param userJwt Пользователь.
     * @return Строковое предствление токена.
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

        Date toDate = new Date(System.currentTimeMillis() + config.getExpMillis());
        LocalDateTime localToDate = toDate.toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();

        String token = Jwts.builder()
                .setClaims(claims)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + config.getExpMillis()))
                .signWith(SignatureAlgorithm.forName(config.getAlgorithm()), secretKey)
                .compact();

        TokenStatus status = tokenStatusService.findById(ACTIVE.getId());
        JwtToken jwtToken = new JwtToken();
        jwtToken.setUserJwt(userJwt);
        jwtToken.setToken(token);
        jwtToken.setTokenStatus(status);
        jwtToken.setFromDate(LocalDateTime.now());
        jwtToken.setToDate(localToDate);
        jwtTokenRepository.save(jwtToken);
        log.info("Token has been generated for the user {}.", userJwt.getLogin());
        return token;
    }

    public JwtConfig getCurrentConfig() {
        return jwtConfigRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("JWT configuration not found"));
    }

    /**
     * Обнговленте конфигурации токена.
     *
     * @param newConfig Новые настройки.
     */
    @Transactional
    public void updateConfig(JwtConfig newConfig) {
        JwtConfig currentConfig = getCurrentConfig();
        currentConfig.setAlgorithm(newConfig.getAlgorithm());
        currentConfig.setKey(newConfig.getKey());
        currentConfig.setExpMillis(newConfig.getExpMillis());
        jwtConfigRepository.save(currentConfig);
        log.info("JWT configuration updated successfully");
    }

    @Transactional
    public List<JwtToken> getTokensByUser(UserJwt userJwt) {
        return jwtTokenRepository.findAllByUserJwt(userJwt);
    }

    /**
     * Валидация токена.
     *
     * @param token Строковое представление токена.
     * @return результат валидации.
     */
    public TokenValidationResult validateToken(String token) {
        log.info("Start of token validation");
        try {
            JwtConfig jwtConfig = jwtConfigRepository.findAll().stream()
                    .findFirst()
                    .orElseThrow(() -> new IllegalStateException("JWT configuration not found"));

            Claims claims = Jwts.parser()
                    .setSigningKey(jwtConfig.getKey().getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token)
                    .getBody();
//            Claims claims = getClaimsFromToken(token);
//            RoleStub role = RoleStub.findByRoleName(claims.get("role", String.class));

            List<JwtToken> tokens = jwtTokenRepository.findAllByToken(token);

            if (tokens.isEmpty()) {
                log.warn("JWT token is not valid");
                return TokenValidationResult.notValid();
            } else {
                JwtToken jwtToken = tokens.getFirst();
                RoleType role = RoleType.findByRole(jwtToken.getUserJwt().getRole());
                boolean isActive = ACTIVE_STATUSES.contains(TokenStatusType.findByStatus(jwtToken.getTokenStatus()));
                boolean notExpired = jwtToken.getToDate().isAfter(LocalDateTime.now());
                if (isActive && notExpired) {
                    log.debug("JWT token is valid");
                    return TokenValidationResult.valid(role, jwtToken.getTokenStatus());
                } else {
                    log.debug("JWT token is not valid");
                    return TokenValidationResult.notValid();
                }
            }
        } catch (Exception e) {
            log.debug("JWT token is not valid");
            return TokenValidationResult.notValid();
        }
    }

    @Transactional
    public void markTokensAsDeletedByUser(UserJwt userJwt) {
        List<JwtToken> tokens = getTokensByUser(userJwt);
        TokenStatus status = tokenStatusService.findById(TokenStatusType.DELETED.getId());
        tokens.forEach(token -> token.setTokenStatus(status));
        jwtTokenRepository.saveAll(tokens);
        log.info("tokens have been removed for the user {}", userJwt.getLogin());
    }

}
