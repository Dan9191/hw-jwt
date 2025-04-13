package com.example.hw_jwt.controller.user;

import com.example.hw_jwt.entity.JwtConfig;
import com.example.hw_jwt.entity.RoleType;
import com.example.hw_jwt.model.TokenValidationResult;
import com.example.hw_jwt.service.JwtService;
import com.example.hw_jwt.view.SendType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/user/operations")
@RequiredArgsConstructor
@Slf4j
public class UserOperationsController {

    private final JwtService jwtService;

    @GetMapping
    public String adminPage(Model model,
                            @CookieValue(value = "JWT", required = false) String token,
                            RedirectAttributes redirectAttributes,
                            HttpServletResponse response) {
        if (tokenIsNotValidVerification(token, response)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка валидации токена");
            return "redirect:/login";
        }
        model.addAttribute("token", token);
        model.addAttribute("sendTypes", SendType.values());
        return "/user/operations";
    }

    @PostMapping("/send")
    public String sendToken(@RequestParam(name = "sendTypes", required = false) List<String> sendTypeNames,
                            @ModelAttribute JwtConfig config,
                            @CookieValue("JWT") String token,
                            RedirectAttributes redirectAttributes,
                            HttpServletResponse response) {
        if (tokenIsNotValidVerification(token, response)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка валидации токена");
            return "redirect:/login";
        }
        if (sendTypeNames== null || sendTypeNames.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Должен быть выбран хотя бы 1 способ отправки");
        } else {
            //todo реализовать отправки
            redirectAttributes.addFlashAttribute("message", "Отправка удалась");
            log.info("Sending successful");
        }
        return "redirect:/user/operations";
    }

    private boolean tokenIsNotValidVerification(@CookieValue("JWT") String token, HttpServletResponse response) {
        if (token == null) {
            Cookie cookie = new Cookie("JWT", null);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            return true;
        }
        TokenValidationResult tokenValidationResult = jwtService.validateToken(token);
        if (!tokenValidationResult.isValid() || !tokenValidationResult.role().equals(RoleType.USER)) {
            Cookie cookie = new Cookie("JWT", null);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            return true;
        }
        return false;
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("JWT", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/login";
    }
}
