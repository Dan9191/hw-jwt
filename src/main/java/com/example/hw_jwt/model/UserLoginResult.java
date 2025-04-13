package com.example.hw_jwt.model;

import com.example.hw_jwt.entity.RoleStub;
import com.example.hw_jwt.entity.UserJwt;

/**
 * Результат создания пользователя.
 */
public record UserLoginResult(boolean success, String token, RoleStub roleType, String errorMessage) {

    public static UserLoginResult success(String token,RoleStub roleType) {
        return new UserLoginResult(true, token, roleType, null);
    }

    public static UserLoginResult failure(String errorMessage) {
        return new UserLoginResult(false, null, null, errorMessage);
    }
}
