package com.example.hw_jwt.controller;

import com.example.hw_jwt.model.UserLoginResult;
import com.example.hw_jwt.service.UserService;
import com.example.hw_jwt.view.LoginUserView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/create-token")
@RequiredArgsConstructor
public class TokenController {

    /**
     * Сервис по работе с пользователями.
     */
    private final UserService userService;

    @GetMapping
    public String showLoginForm(Model model) {
        model.addAttribute("createToken", new LoginUserView());
        return "/create-token";
    }

    @PostMapping
    public String processLogin(
            @ModelAttribute("createToken") LoginUserView loginUser,
            @RequestParam("action") String action,
            Model model) {

        if ("send".equals(action)) {
            if (loginUser.getSendTypeList() == null || loginUser.getSendTypeList().isEmpty()) {
                model.addAttribute("errorMessage", "Должен быть выбран хотя бы 1 способ отправки");
            }
            UserLoginResult userLoginResult = userService.loginUser(loginUser);
            if (userLoginResult == null) {
                model.addAttribute("errorMessage", "Невозможно завершить операцию");
            } else if (userLoginResult.success()) {
                model.addAttribute("message", userLoginResult.token());
            } else {
                model.addAttribute("errorMessage",userLoginResult.errorMessage());
            }
        } else {
            model.addAttribute("errorMessage", "Неизвестная операция");
        }

        return "/create-token";
    }
}
