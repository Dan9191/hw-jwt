package com.example.hw_jwt.entity;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.Data;

import java.time.LocalDateTime;

/**
 * Сущность для хранения ссылки.
 */
@Entity
@Table(name = "jwt_token")
@Data
public class JwtToken {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne(fetch = FetchType.EAGER, optional = false)
    @JoinColumn(name = "user_jwt_id", nullable = false)
    private UserJwt userJwt;

    @Column(name = "token", nullable = false, unique = true)
    private String token;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "token_status_id", nullable = false)
    private TokenStatus tokenStatus;

    @Column(name = "from_date", columnDefinition = "TIMESTAMP DEFAULT NOW()")
    private LocalDateTime fromDate;

    @Column(name = "to_date")
    private LocalDateTime toDate;

}
