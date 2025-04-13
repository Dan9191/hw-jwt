package com.example.hw_jwt.exception;

/**
 * Ошибка при отсутствии пользователя.
 */
public class ValidTokenException extends RuntimeException {

    private static final String message = "Токен не валидный";

    public ValidTokenException() {
        super(message);
    }
}
