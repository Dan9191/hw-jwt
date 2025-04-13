package com.example.hw_jwt.model;

import com.example.hw_jwt.entity.RoleType;
import com.example.hw_jwt.entity.TokenStatus;

import java.time.LocalDateTime;

/**
 * Описание результата валидации токена.
 */
public record TokenValidationResult(
        boolean isValid, // валиден ли токен
        RoleType role, // какому типу юзеров принадлежит токен
        TokenStatus tokenStatus // статус токена
) {

    public static TokenValidationResult valid(RoleType role, TokenStatus tokenStatus) {
        return new TokenValidationResult(true, role, tokenStatus);
    }

    public static TokenValidationResult notValid() {
        return new TokenValidationResult(false, null, null);
    }
}
