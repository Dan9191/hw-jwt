-- выбор схемы для выполнения скрипта
SET search_path TO jwt_service;

-- Создаем таблицу role
CREATE TABLE IF NOT EXISTS role (
                      id INTEGER PRIMARY KEY,
                      name VARCHAR(50) NOT NULL UNIQUE
);

-- Вставляем две стандартные роли
INSERT INTO role (id, name) VALUES
                                (1, 'ADMIN'),
                                (2, 'USER');
INSERT INTO role (id, name)
SELECT 1, 'ADMIN'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE id = 1);

INSERT INTO role (id, name)
SELECT 2, 'USER'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE id = 2);

INSERT INTO role (id, name)
SELECT 3, 'DELETED'
WHERE NOT EXISTS (SELECT 1 FROM role WHERE id = 3);


-- Создаем таблицу user
CREATE TABLE IF NOT EXISTS user_jwt (
                        id BIGSERIAL PRIMARY KEY,
                        login VARCHAR(100) NOT NULL UNIQUE,
                        password VARCHAR(255) NOT NULL,
                        role_id INTEGER NOT NULL,
                        FOREIGN KEY (role_id) REFERENCES role(id)
);

-- Создаем таблицу jwt_config
CREATE TABLE IF NOT EXISTS jwt_config (
                          id BIGSERIAL PRIMARY KEY,
                          algorithm VARCHAR(100) NOT NULL UNIQUE,
                          key VARCHAR(100) NOT NULL UNIQUE,
                          exp_millis BIGINT NOT NULL
);
