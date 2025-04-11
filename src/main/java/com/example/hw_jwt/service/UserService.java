package com.example.hw_jwt.service;

import com.example.hw_jwt.entity.Role;
import com.example.hw_jwt.entity.UserJwt;
import com.example.hw_jwt.model.UserCreationResult;
import com.example.hw_jwt.model.UserLoginResult;
import com.example.hw_jwt.repository.UserJwtRepository;
import com.example.hw_jwt.view.CreateUserView;
import com.example.hw_jwt.view.LoginUserView;
import com.example.hw_jwt.view.RoleStub;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

/**
 * Сервис по работе с пользователями.
 */
@Service
@RequiredArgsConstructor
@Slf4j
public class UserService {

    private final UserJwtRepository userJwtRepository;

    private final RoleService roleService;

    private final JwtService jwtService;

    /**
     * Создание пользователя.
     *
     * @param createUserView Представление пользователя.
     * @return Результат создания пользователя.
     */
    @Transactional
    public UserCreationResult createUser(CreateUserView createUserView) {
        log.info("Начало создания пользователя {}", createUserView.getLogin());
        // Проверка уникальности логина
        if (userJwtRepository.existsByLogin(createUserView.getLogin())) {
            log.warn("Логин уже занят {}", createUserView.getLogin());
            return UserCreationResult.failure("Логин уже занят");
        }

        RoleStub roleStub = createUserView.getRoleStub();
        // Проверка на единственного администратора
        if (roleStub == RoleStub.ADMIN && !userJwtRepository.findByRoleId(RoleStub.ADMIN.getId()).isEmpty()) {
            log.warn("Роль администратора уже занята");
            return UserCreationResult.failure("Администратор уже существует");
        }

        Role role = roleService.findById(roleStub.getId());
        UserJwt user = new UserJwt();
        user.setLogin(createUserView.getLogin());
        user.setPassword(createUserView.getPassword());
        user.setRole(role);

        userJwtRepository.save(user);
        log.info("Пользователь {} успешно добавлен", createUserView.getLogin());
        return UserCreationResult.success(user);
    }

    public UserLoginResult loginUser(LoginUserView loginUser) {
        Optional<UserJwt> userJwt =
                userJwtRepository.findByLoginAndPassword(loginUser.getLogin(), loginUser.getPassword());
        return userJwt.map(jwt -> UserLoginResult.success(jwtService.generateToken(jwt)))
                .orElseGet(() -> UserLoginResult.failure("Не удалось найти пользователя по такой комбинации"));
    }

    @Transactional
    public UserJwt getUser(String login) {
        return userJwtRepository.getByLogin(login);
    }


}
