package com.example.hw_jwt.controller;

import com.example.hw_jwt.service.JwtService;
import com.example.hw_jwt.view.ValidateTokenView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/validate-token")
@RequiredArgsConstructor
public class ValidateToken {

    /**
     * Сервис по работе с пользователями.
     */
    private final JwtService jwtService;

    @GetMapping()
    public String showValidationPage(Model model) {
        model.addAttribute("tokenRequest", new ValidateTokenView());
        return "validate-token";
    }

    @PostMapping
    public String processLogin(
            @ModelAttribute("createToken") ValidateTokenView tokenView,
            Model model) {

            boolean isValid = jwtService.validateToken(tokenView.getToken());
            model.addAttribute("validationResult", isValid ? "Токен валиден" : "Токен невалиден");

            return "/validate-token";
    }
}
