package com.example.hw_jwt.view;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * Модель для авторизации пользователя.
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class LoginUserView {

    private String login;
    private String password;
    private List<SendType> sendTypeList;

}
