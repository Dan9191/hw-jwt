package com.example.hw_jwt.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Тип пользователя.
 */
@RequiredArgsConstructor
@Getter
public enum TokenStatusStub {
    ACTIVE(1, "Код активен"),
    EXPIRED(2, "Код просрочен"),
    USED(3, "Используется"),
    DELETED(3, "Удален");

    private final int id;
    private final String label;

}
