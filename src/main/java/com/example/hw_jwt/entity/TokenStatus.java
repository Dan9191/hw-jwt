package com.example.hw_jwt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.NoArgsConstructor;

/**
 * Сущность для хранения ссылки.
 */
@Entity
@Table(name = "token_status")
@NoArgsConstructor
@Getter
public class TokenStatus {
    @Id
    @Column(name = "id")
    private Integer id;

    @Column(name = "status", nullable = false, unique = true)
    private String status;

}
