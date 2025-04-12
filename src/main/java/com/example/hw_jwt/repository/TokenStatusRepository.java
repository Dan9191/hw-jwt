package com.example.hw_jwt.repository;

import com.example.hw_jwt.entity.TokenStatus;
import org.springframework.data.jpa.repository.JpaRepository;

public interface TokenStatusRepository extends JpaRepository<TokenStatus, Long> {
}
