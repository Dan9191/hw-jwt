package com.example.hw_jwt.view;

import lombok.Getter;

/**
 * Способ отправки.
 */
@Getter
public enum SendType {
    EMAIL("email"),
    TELEGRAM("telegram"),
    SMPP("telephone"),
    FILE("file");

    private final String label;

    SendType(String label) {
        this.label = label;
    }

}
