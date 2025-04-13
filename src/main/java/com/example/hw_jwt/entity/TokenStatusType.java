package com.example.hw_jwt.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Тип пользователя.
 */
@RequiredArgsConstructor
@Getter
public enum TokenStatusType {
    ACTIVE(1, "Код активен"),
    EXPIRED(2, "Код просрочен"),
    USED(3, "Используется"),
    DELETED(3, "Удален");

    private final int id;
    private final String label;

    public static final List<TokenStatusType> ACTIVE_STATUSES = Stream.of(ACTIVE, USED).collect(Collectors.toList());

    private static final TokenStatusType[] VALUES = values();
    /**
     * Поиск значения перечисления по сущности БД.
     *
     * @param status сущность в БД
     * @return перечисление или null, если перечисление не найдено
     */
    public static TokenStatusType findByStatus(TokenStatus status) {
        if (status == null) {
            return null;
        }
        return Arrays.stream(VALUES)
                .filter(item -> Objects.equals(item.id, status.getId()))
                .findFirst()
                .orElse(null);
    }



}
