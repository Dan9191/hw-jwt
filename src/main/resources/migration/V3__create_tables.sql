SET search_path TO jwt_service;

-- Создаем таблицу token_status
CREATE TABLE IF NOT EXISTS token_status (
                      id INTEGER PRIMARY KEY,
                      status VARCHAR(50) NOT NULL UNIQUE
);

-- Вставляем две стандартные статусы
INSERT INTO token_status (id, status)
SELECT 1, 'ACTIVE'
WHERE NOT EXISTS (SELECT 1 FROM token_status WHERE id = 1);

INSERT INTO token_status (id, status)
SELECT 2, 'EXPIRED'
WHERE NOT EXISTS (SELECT 1 FROM token_status WHERE id = 2);

INSERT INTO token_status (id, status)
SELECT 3, 'USED'
WHERE NOT EXISTS (SELECT 1 FROM token_status WHERE id = 3);

INSERT INTO token_status (id, status)
SELECT 4, 'DELETED'
WHERE NOT EXISTS (SELECT 1 FROM token_status WHERE id = 4);


CREATE TABLE IF NOT EXISTS jwt_token (
                           id SERIAL PRIMARY KEY,
                           user_jwt_id INTEGER REFERENCES user_jwt (id) ON DELETE CASCADE,
                           token_status_id INTEGER REFERENCES token_status (id),
                           token VARCHAR NOT NULL UNIQUE,
                           from_date TIMESTAMP NOT NULL DEFAULT NOW(),
                           to_date TIMESTAMP NOT NULL
);
