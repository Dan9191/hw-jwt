package com.example.hw_jwt.controller.admin;

import com.example.hw_jwt.entity.JwtConfig;
import com.example.hw_jwt.entity.JwtToken;
import com.example.hw_jwt.entity.RoleType;
import com.example.hw_jwt.entity.UserJwt;
import com.example.hw_jwt.model.TokenValidationResult;
import com.example.hw_jwt.service.JwtService;
import com.example.hw_jwt.service.UserService;
import com.example.hw_jwt.service.send.EmailNotificationService;
import com.example.hw_jwt.service.send.SmppNotificationService;
import com.example.hw_jwt.service.send.TelegramNotificationService;
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
public class AdminOperationsController {

    private final JwtService jwtService;

    private final UserService userService;

    private final EmailNotificationService emailNotificationService;

    private final SmppNotificationService smppNotificationService;

    private final TelegramNotificationService telegramNotificationService;

    @GetMapping
    public String adminPage(Model model,
                            @CookieValue(value = "JWT", required = false) String token,
                            RedirectAttributes redirectAttributes,
                            HttpServletResponse response) {
        if (tokenIsNotValidVerification(token, response)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка валидации токена");
            return "redirect:/login";
        }
        model.addAttribute("config", jwtService.getCurrentConfig());
        model.addAttribute("token", token);
        model.addAttribute("sendTypes", SendType.values());
        model.addAttribute("users", userService.getAllUserWithoutAdmin());
        model.addAttribute("roles", RoleType.values());
        return "/admin/operations";
    }

    @PostMapping("/send")
    public String sendToken(@RequestParam(name = "sendTypes", required = false) List<String> sendTypeNames,
                            @ModelAttribute JwtConfig config,
                            @CookieValue("JWT") String token,
                            RedirectAttributes redirectAttributes,
                            HttpServletResponse response) {
        String errorMessage = "";
        if (tokenIsNotValidVerification(token, response)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка валидации токена");
            return "redirect:/login";
        }
        if (sendTypeNames == null || sendTypeNames.isEmpty()) {
            redirectAttributes.addFlashAttribute("errorMessage", "Должен быть выбран хотя бы 1 способ отправки");
        } else {
            JwtToken jwtToken = jwtService.getJwtTokenByCode(token);
            try {
                if (sendTypeNames.contains("EMAIL")) {
                    try {
                    emailNotificationService.sendCode(jwtToken.getUserJwt().getEmail(), token);
                    log.info("the code has been sent to the mail {}", jwtToken.getUserJwt().getEmail());
                    } catch (Exception e) {
                        log.error("error sending to mail {}", e.getMessage());
                        errorMessage = errorMessage + "\nОшибка отправки по электронной почте";
                    }
                }
                if (sendTypeNames.contains("SMPP")) {
                    try {
                        smppNotificationService.sendSms(jwtToken.getUserJwt().getTelephone(), token);
                        log.info("the code has been sent to the telephone {}", jwtToken.getUserJwt().getTelephone());
                    } catch (Exception e) {
                        log.error("error sending to telephone {}", e.getMessage());
                        errorMessage = errorMessage + "\nОшибка отправки по телефону";
                    }
                }
                if (sendTypeNames.contains("TELEGRAM")) {
                    try {
                        telegramNotificationService.sendCode(jwtToken.getUserJwt().getTelegram(), token);
                        log.info("the code has been sent to the telegram {}", jwtToken.getUserJwt().getTelegram());
                    } catch (Exception e) {
                        log.error("error sending to telegram {}", e.getMessage());
                        errorMessage = errorMessage + "\nОшибка отправки по электронной почте";
                    }
                }
                if (sendTypeNames.contains("FILE")) {
                    response.setContentType("text/plain");
                    response.setHeader("Content-Disposition", "attachment; filename=\"Your_OTP_Code.txt\"");
                    response.getWriter().write("Your verification code is: " + token);
                    return null;
                }
                if (errorMessage.isEmpty()) {
                    redirectAttributes.addFlashAttribute("message", "Отправка удалась");
                } else {
                    redirectAttributes.addFlashAttribute("errorMessage", errorMessage);
                }
                log.info("Sending successful");
            } catch (Exception e) {
                throw new RuntimeException(e);
            }
        }
        return "redirect:/admin/operations";
    }

    @PostMapping("/{id}/delete")
    public String markAsDeleted(@PathVariable Long id,
                                @CookieValue("JWT") String token,
                                RedirectAttributes redirectAttributes,
                                HttpServletResponse response) {
        if (tokenIsNotValidVerification(token, response)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка валидации токена");
            return "redirect:/login";
        }
        userService.markUserAsDeleted(id);
        redirectAttributes.addFlashAttribute("message", "Пользователь успешно удален");
        log.info("User successfully deleted");
        return "redirect:/admin/operations";
    }

    @PostMapping("/update")
    public String updateConfig(@ModelAttribute JwtConfig config,
                               @CookieValue("JWT") String token,
                               RedirectAttributes redirectAttributes,
                               HttpServletResponse response) {
        if (tokenIsNotValidVerification(token, response)) {
            redirectAttributes.addFlashAttribute("errorMessage", "Ошибка валидации токена");
            return "redirect:/login";
        }
        jwtService.updateConfig(config);
        redirectAttributes.addFlashAttribute("message", "Конфигурации успешно обновлена");
        log.info("Token configuration updated successfully");
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
        if (!tokenValidationResult.isValid() || !tokenValidationResult.role().equals(RoleType.ADMIN)) {
            Cookie cookie = new Cookie("JWT", null);
            cookie.setPath("/");
            cookie.setHttpOnly(true);
            cookie.setMaxAge(0);
            response.addCookie(cookie);
            return true;
        }
        return false;
    }
}
