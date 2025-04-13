package com.example.hw_jwt.controller.user;

import com.example.hw_jwt.entity.JwtConfig;
import com.example.hw_jwt.entity.RoleStub;
import com.example.hw_jwt.view.SendType;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
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
public class OperationsPage {


    @GetMapping
    public String adminPage(Model model,
                            @CookieValue(value = "JWT", required = false) String token) {
        if (token == null) return "redirect:/login";
        model.addAttribute("token", token);
        model.addAttribute("sendTypes", SendType.values());
        return "/user/operations";
    }

    @PostMapping("/send")
    public String sendToken(@RequestParam(name = "sendTypes", required = false) List<String> sendTypeNames,
                            @ModelAttribute JwtConfig config,
                            @CookieValue("JWT") String token,
                            RedirectAttributes redirectAttributes) {
        if (token == null) return "redirect:/login";
        if (sendTypeNames== null || sendTypeNames.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Должен быть выбран хотя бы 1 способ отправки");
        } else {
            //todo реализовать отправки
            redirectAttributes.addFlashAttribute("message", "Отправка удалась");
        }
        return "redirect:/user/operations";
    }

    @PostMapping("/logout")
    public String logout(HttpServletResponse response) {
        // Создаем пустой cookie с тем же именем
        Cookie cookie = new Cookie("JWT", null);
        cookie.setPath("/");
        cookie.setHttpOnly(true);
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        return "redirect:/login";
    }

    private boolean validateCookies(String token) {
        // todo сделать валидацию токена для каждой операции
        return true;
    }
}
