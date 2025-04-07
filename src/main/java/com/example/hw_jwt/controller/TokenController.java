package com.example.hw_jwt.controller;

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
            } else {
                System.out.println(loginUser);
                model.addAttribute("message", "Токен отправлен");
            }
        } else {
            model.addAttribute("errorMessage", "Неизвестная операция");
        }

        return "/create-token";
    }
}
