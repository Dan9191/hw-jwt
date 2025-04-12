package com.example.hw_jwt.service;

import com.example.hw_jwt.entity.JwtConfig;
import com.example.hw_jwt.entity.JwtToken;
import com.example.hw_jwt.entity.Role;
import com.example.hw_jwt.entity.TokenStatus;
import com.example.hw_jwt.entity.TokenStatusStub;
import com.example.hw_jwt.entity.UserJwt;
import com.example.hw_jwt.repository.JwtConfigRepository;
import com.example.hw_jwt.repository.JwtTokenRepository;
import com.example.hw_jwt.repository.TokenStatusRepository;
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

import static com.example.hw_jwt.entity.TokenStatusStub.ACTIVE;

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

        return token;
    }

    public JwtConfig getCurrentConfig() {
        return jwtConfigRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("JWT configuration not found"));
    }

    @Transactional
    public void updateConfig(JwtConfig newConfig) {
        JwtConfig currentConfig = getCurrentConfig();
        currentConfig.setAlgorithm(newConfig.getAlgorithm());
        currentConfig.setKey(newConfig.getKey());
        currentConfig.setExpMillis(newConfig.getExpMillis());
        jwtConfigRepository.save(currentConfig);
    }

    @Transactional
    public List<JwtToken> getTokensByUser(UserJwt userJwt) {
        return jwtTokenRepository.findAllByUserJwt(userJwt);
    }

    public boolean validateToken(String token) {

        JwtConfig jwtConfig = jwtConfigRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("JWT configuration not found"));

        try {
            Jwts.parser()
                    .setSigningKey(jwtConfig.getKey().getBytes(StandardCharsets.UTF_8))
                    .parseClaimsJws(token);
            Claims claims = getClaimsFromToken(token);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    @Transactional
    public void markTokensAsDeletedByUser(UserJwt userJwt) {
        List<JwtToken> tokens = getTokensByUser(userJwt);
        TokenStatus status = tokenStatusService.findById(TokenStatusStub.DELETED.getId());
        tokens.forEach(token -> token.setTokenStatus(status));
        jwtTokenRepository.saveAll(tokens);
    }
    /**
     * Разбирает токен на его состовляющие.
     *
     * @param token Токен в виде строки.
     * @return параметры токена.
     */
    public Claims getClaimsFromToken(String token) {

        JwtConfig jwtConfig = jwtConfigRepository.findAll().stream()
                .findFirst()
                .orElseThrow(() -> new IllegalStateException("JWT configuration not found"));

        return Jwts.parser()
                .setSigningKey(jwtConfig.getKey().getBytes(StandardCharsets.UTF_8))
                .parseClaimsJws(token)
                .getBody();
    }

//    public boolean isTokenExpired(String token) {
//        Date expiration = getClaimsFromToken(token).getExpiration();
//        return expiration.before(new Date());
//    }

}
