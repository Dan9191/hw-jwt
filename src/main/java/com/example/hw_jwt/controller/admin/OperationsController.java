package com.example.hw_jwt.controller.admin;

import com.example.hw_jwt.view.SendType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@RequestMapping("/admin/operations")
@RequiredArgsConstructor
public class OperationsController {

    @GetMapping
    public String adminPage(Model model,
                            @CookieValue(value = "JWT", required = false) String token) {
        if (token == null) return "redirect:/login";

        model.addAttribute("token", token);
        model.addAttribute("sendTypes", SendType.values());
        return "/admin/operations";
    }

    @PostMapping()
    public String sendToken(@RequestParam(name = "sendTypes", required = false) List<String> sendTypeNames,
                            @RequestParam("action") String action,
                            @CookieValue("JWT") String token,
                            Model model) {
        model.addAttribute("token", token);
        model.addAttribute("sendTypes", SendType.values());
        if ("send".equals(action)) {
            if (sendTypeNames== null || sendTypeNames.isEmpty()) {
                model.addAttribute("errorMessage", "Должен быть выбран хотя бы 1 способ отправки");
            } else {
                //todo реализовать отправки
                model.addAttribute("message", "Отправка удалась");
            }
            return "/admin/operations";
        }
        return "redirect:/admin/operations";
    }
}
