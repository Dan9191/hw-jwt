package com.example.hw_jwt.model;

import com.example.hw_jwt.entity.UserJwt;

/**
 * Результат создания пользователя.
 */
public record UserCreationResult(boolean success, UserJwt user, String errorMessage) {

    public static UserCreationResult success(UserJwt user) {
        return new UserCreationResult(true, user, null);
    }

    public static UserCreationResult failure(String errorMessage) {
        return new UserCreationResult(false, null, errorMessage);
    }
}
