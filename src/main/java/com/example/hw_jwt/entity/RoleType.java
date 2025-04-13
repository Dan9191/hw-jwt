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
public enum RoleType {
    ADMIN(1, "Администратор"),
    USER(2, "Пользователь"),
    DELETED(3, "Удален");

    private final int id;
    private final String label;

    private static final RoleType[] VALUES = values();

    /**
     * Поиск значения перечисления по сущности БД.
     *
     * @param role сущность в БД
     * @return перечисление или null, если перечисление не найдено
     */
    public static RoleType findByRole(Role role) {
        if (role == null) {
            return null;
        }
        return Arrays.stream(VALUES)
                .filter(item -> Objects.equals(item.id, role.getId()))
                .findFirst()
                .orElse(null);
    }

    /**
     * Поиск значения перечисления по сущности БД.
     *
     * @param role сущность в БД
     * @return перечисление или null, если перечисление не найдено
     */
    public static RoleType findByRoleName(String role) {
        if (role == null) {
            return null;
        }
        return Arrays.stream(VALUES)
                .filter(item -> Objects.equals(item.name(), role))
                .findFirst()
                .orElse(null);
    }

}
