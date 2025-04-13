package com.example.hw_jwt.controller.admin;

import com.example.hw_jwt.entity.JwtConfig;
import com.example.hw_jwt.entity.RoleStub;
import com.example.hw_jwt.service.JwtService;
import com.example.hw_jwt.service.UserService;
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
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.util.List;

@Controller
@RequestMapping("/admin/operations")
@RequiredArgsConstructor
@Slf4j
public class OperationsController {

    private final JwtService jwtService;

    private final UserService userService;

    @GetMapping
    public String adminPage(Model model,
                            @CookieValue(value = "JWT", required = false) String token) {
        if (token == null) return "redirect:/login";
        model.addAttribute("config", jwtService.getCurrentConfig());
        model.addAttribute("token", token);
        model.addAttribute("sendTypes", SendType.values());
        model.addAttribute("users", userService.getAllUserWithoutAdmin());
        model.addAttribute("roles", RoleStub.values());
        return "/admin/operations";
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
        return "redirect:/admin/operations";
    }

    @PostMapping("/{id}/delete")
    public String markAsDeleted(@PathVariable Long id,
                                @CookieValue("JWT") String token,
                                RedirectAttributes redirectAttributes) {
        if (token == null) return "redirect:/login";
        userService.markUserAsDeleted(id);
        redirectAttributes.addFlashAttribute("message", "Пользователь успешно удален");
        return "redirect:/admin/operations";
    }

    @PostMapping("/update")
    public String updateConfig(@ModelAttribute JwtConfig config,
                               @CookieValue("JWT") String token,
                               RedirectAttributes redirectAttributes) {
        jwtService.updateConfig(config);
        redirectAttributes.addFlashAttribute("message", "Конфигурации успешно обновлена");
        return "redirect:/admin/operations";
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
