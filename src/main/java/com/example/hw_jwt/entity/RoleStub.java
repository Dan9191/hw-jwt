package com.example.hw_jwt.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Тип пользователя.
 */
@RequiredArgsConstructor
@Getter
public enum RoleStub {
    ADMIN(1, "Администратор"),
    USER(2, "Пользователь"),
    DELETED(3, "Удален");

    private final int id;
    private final String label;

}
