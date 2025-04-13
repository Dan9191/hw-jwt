package com.example.hw_jwt.controller.user;

import com.example.hw_jwt.view.SendType;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
    public String sendToken(@RequestParam SendType sendType,
                            @CookieValue("JWT") String token) {
        return "redirect:/user/operations";
    }
}
