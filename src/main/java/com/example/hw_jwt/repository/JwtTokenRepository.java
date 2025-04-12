package com.example.hw_jwt.repository;

import com.example.hw_jwt.entity.JwtToken;
import com.example.hw_jwt.entity.UserJwt;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;


public interface JwtTokenRepository extends JpaRepository<JwtToken, Long> {

    @Modifying
    @Transactional
    @Query(value = """
    UPDATE jwt_token jt
    SET token_status_id = (SELECT id FROM token_status WHERE status = 'EXPIRED')
    WHERE jt.id IN (
        SELECT jt.id 
        FROM jwt_token jt
        JOIN token_status ts ON jt.token_status_id = ts.id
        WHERE ts.status IN ('ACTIVE', 'USED')
          AND jt.to_date < CURRENT_TIMESTAMP
    )
    """, nativeQuery = true)
    void markExpiredTokensAsExpired();

    List<JwtToken> findAllByUserJwt(UserJwt userJwt);
}
