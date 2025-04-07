package com.example.hw_jwt.view;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

/**
 * Тип пользователя.
 */
@RequiredArgsConstructor
@Getter
public enum RoleStub {
    ADMIN(1, "Администратор"),
    USER(2, "Пользователь");

    private final int id;
    private final String label;

}
