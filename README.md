## Сервис выполнения операций, защищенных токеном

### Действия перед началом
* Регистрация в mail.ru и получение пароля пользователя
* Получить chat_id телеграмма
* Создать бота в телеграмм и получить его токен
* Запустить эмулятор SMPPsim
* Запустить БД postgresql

### Описание всех переменных окружения :

| Переменная                                 | Значение переменной (по-умолчанию)                                             | Описание                                                                                   |
|--------------------------------------------|--------------------------------------------------------------------------------|--------------------------------------------------------------------------------------------|
| spring.datasource.url                      | DATASOURCE_URL:jdbc:postgresql://localhost:5432/test?currentSchema=jwt_service | Строка подключения к БД                                                                    |
| spring.datasource.username                 | user                                                                           | Пользователь БД                                                                            |
| spring.datasource.password                 | user                                                                           | Пароль БД                                                                                  |
| token.properties.algorithm                 | HS256                                                                          | алгоритм кодирования токена                                                                |
| token.properties.exp-millis                | 36000                                                                          | время жизни токена                                                                         |
| token.properties.secret-key                | 565521c44f61dddac02f0d84fd9659ad987193dc8783d8cdf9d95a55ad302b2d               | секретный ключ генерации токена                                                            |
| token.properties.configuration-change-sign | false                                                                          | Требуется ли при каждом запуске сервиса уситанавливать конфигурацию из настроек приложения |
| mail.properties.from                       | вставить свое                                                                  | почтовый адрес mail.ru                                                                     |
| mail.properties.password                   | вставить свое                                                                  | пароль от почты                                                                            |
| mail.properties.host                       | smtp.mail.ru                                                                   |                                                                                            |
| mail.properties.smtp-auth-sign             | true                                                                           |                                                                                            |
| mail.properties.smtp-port                  | 465                                                                            |                                                                                            |
| mail.properties.smtp-ssl-enable            | true                                                                           |                                                                                            |
| smpp.properties.host                       | localhost                                                                      | Настройки SMPP эмулятора                                                                   |
| smpp.properties.port                       | 2775                                                                           |                                                                                            |
| smpp.properties.password                   | password                                                                       |                                                                                            |
| smpp.properties.system-id                  | smppclient1                                                                    |                                                                                            |
| smpp.properties.source-addr                | OTPService                                                                     |                                                                                            |
| smpp.properties.system-type                | OTP                                                                            |                                                                                            |
| telegram.properties.token                  | вставить свое                                                                  | токен бота телеграмм                                                                       |
