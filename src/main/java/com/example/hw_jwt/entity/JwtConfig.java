package com.example.hw_jwt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

/**
 * Сущность для хранения ссылки.
 */
@Entity
@Table(name = "jwt_config")
@Data
public class JwtConfig {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "algorithm", nullable = false, unique = true)
    private String algorithm;

    @Column(name = "key", nullable = false, unique = true)
    private String key;

    @Column(name = "exp_millis",  nullable = false)
    private Long expMillis;
}
