package com.example.hw_jwt.repository;

import com.example.hw_jwt.entity.JwtConfig;
import org.springframework.data.jpa.repository.JpaRepository;

public interface JwtConfigRepository extends JpaRepository<JwtConfig, Long> {
}
