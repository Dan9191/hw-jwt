-- выбор схемы для выполнения скрипта
SET search_path TO jwt_service;

-- Создаем таблицу role
CREATE TABLE role (
                      id INTEGER PRIMARY KEY,
                      name VARCHAR(50) NOT NULL UNIQUE
);

-- Вставляем две стандартные роли
INSERT INTO role (id, name) VALUES
                                (1, 'ADMIN'),
                                (2, 'USER');

-- Создаем таблицу user
CREATE TABLE user_jwt (
                        id BIGSERIAL PRIMARY KEY,
                        login VARCHAR(100) NOT NULL UNIQUE,
                        password VARCHAR(255) NOT NULL,
                        role_id INTEGER NOT NULL,
                        FOREIGN KEY (role_id) REFERENCES role(id)
);
