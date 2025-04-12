package com.example.hw_jwt.controller;

import com.example.hw_jwt.entity.JwtConfig;
import com.example.hw_jwt.service.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/admin/jwt-config")
@RequiredArgsConstructor
public class EditJwtController {

    private final JwtService jwtService;

    @GetMapping
    public String showConfigPage(Model model) {
        model.addAttribute("config", jwtService.getCurrentConfig());
        return "admin/jwt-config";
    }

    @PostMapping("/update")
    public String updateConfig(@ModelAttribute JwtConfig config) {
        jwtService.updateConfig(config);
        return "redirect:/admin/jwt-config";
    }
}
