package com.example.hw_jwt.service;

import com.example.hw_jwt.entity.Role;
import com.example.hw_jwt.entity.RoleType;
import com.example.hw_jwt.entity.UserJwt;
import com.example.hw_jwt.model.UserCreationResult;
import com.example.hw_jwt.model.UserLoginResult;
import com.example.hw_jwt.repository.UserJwtRepository;
import com.example.hw_jwt.view.CreateUserView;
import com.example.hw_jwt.view.LoginUserView;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

import static com.example.hw_jwt.entity.RoleType.DELETED;
import static com.example.hw_jwt.entity.RoleType.findByRole;

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
            log.warn("Login {} already taken ", createUserView.getLogin());
            return UserCreationResult.failure("Логин уже занят");
        }

        RoleType roleType = createUserView.getRoleType();
        // Проверка на единственного администратора
        if (roleType == RoleType.ADMIN && !userJwtRepository.findByRoleId(RoleType.ADMIN.getId()).isEmpty()) {
            log.warn("The administrator role is already taken");
            return UserCreationResult.failure("Администратор уже существует");
        }

        Role role = roleService.findById(roleType.getId());
        UserJwt user = new UserJwt();
        user.setLogin(createUserView.getLogin());
        user.setPassword(createUserView.getPassword());
        user.setRole(role);

        userJwtRepository.save(user);
        log.info("User {} added successfully", createUserView.getLogin());
        return UserCreationResult.success(user);
    }

    public UserLoginResult loginUser(LoginUserView loginUser) {
        Optional<UserJwt> userJwt =
                userJwtRepository.findByLoginAndPassword(loginUser.getLogin(), loginUser.getPassword());
        return userJwt.map(user ->
                        UserLoginResult.success(jwtService.generateToken(user), findByRole(user.getRole()))
                )
                .orElseGet(() ->
                        UserLoginResult.failure("Не удалось найти пользователя по такой комбинации")
                );
    }

    @Transactional
    public List<UserJwt> getAllUserWithoutAdmin() {
        return userJwtRepository.findAllNonAdminUsers();
    }

    /**
     * Помечаем пользователя и его токены как "Удаленнные".
     *
     * @param id Идентификатор пользователя.
     */
    @Transactional
    public void markUserAsDeleted(Long id) {
        UserJwt userJwt =  userJwtRepository.getReferenceById(id);
        userJwt.setRole(roleService.findById(DELETED.getId()));
        jwtService.markTokensAsDeletedByUser(userJwt);
        userJwtRepository.save(userJwt);
        log.info("User {} deleted successfully", userJwt.getLogin());
    }
}
