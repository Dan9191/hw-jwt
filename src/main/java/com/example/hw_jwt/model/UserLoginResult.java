package com.example.hw_jwt.model;

import com.example.hw_jwt.entity.UserJwt;

/**
 * Результат создания пользователя.
 */
public record UserLoginResult(boolean success, String token, String errorMessage) {

    public static UserLoginResult success(String token) {
        return new UserLoginResult(true, token, null);
    }

    public static UserLoginResult failure(String errorMessage) {
        return new UserLoginResult(false, null, errorMessage);
    }
}
