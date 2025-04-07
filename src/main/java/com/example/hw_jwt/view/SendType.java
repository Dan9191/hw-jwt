package com.example.hw_jwt.view;

/**
 * Способ отправки.
 */
public enum SendType {
    EMAIL("email"),
    TELEGRAM("telegram"),
    SMMP("telephone"),
    FILE("file");

    private final String label;

    SendType(String label) {
        this.label = label;
    }

    public String getLabel() {
        return label;
    }
}
