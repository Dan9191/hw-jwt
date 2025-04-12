package com.example.hw_jwt.exception;

/**
 * Ошибка при отсутствии пользователя.
 */
public class UserNotFoundException extends RuntimeException {

    private static final String message = "Пользователь не найден";

    public UserNotFoundException() {
        super(message);
    }
}
