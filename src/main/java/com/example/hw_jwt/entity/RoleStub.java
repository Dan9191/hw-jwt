package com.example.hw_jwt.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.Objects;

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

    private static final RoleStub[] VALUES = values();

    /**
     * Поиск значения перечисления по сущности БД.
     *
     * @param role сущность в БД
     * @return перечисление или null, если перечисление не найдено
     */
    public static RoleStub findByRole(Role role) {
        if (role == null) {
            return null;
        }
        return Arrays.stream(VALUES)
                .filter(item -> Objects.equals(item.id, role.getId()))
                .findFirst()
                .orElse(null);
    }

}
