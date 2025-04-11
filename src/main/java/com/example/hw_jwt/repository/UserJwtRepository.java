package com.example.hw_jwt.repository;

import com.example.hw_jwt.entity.UserJwt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface UserJwtRepository extends JpaRepository<UserJwt, Long> {

    @Query("SELECT CASE WHEN COUNT(u) > 0 THEN true ELSE false END FROM UserJwt u WHERE u.login = :login")
    boolean existsByLogin(@Param("login") String login);

    @Query(nativeQuery = true,
            value = "select u.* from jwt_service.user_jwt u where u.role_id in (:roleId)")
    List<UserJwt> findByRoleId(@Param("roleId") Integer id);

    @Query(nativeQuery = true,
            value = "select u.* from jwt_service.user_jwt u where u.login = :login and u.password = :password")
    Optional<UserJwt> findByLoginAndPassword(@Param("login") String login, @Param("password") String password);

    UserJwt getByLogin(String login);
}
