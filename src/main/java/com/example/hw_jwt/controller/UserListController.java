package com.example.hw_jwt.controller;

import com.example.hw_jwt.entity.RoleStub;
import com.example.hw_jwt.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("admin/users-table")
@RequiredArgsConstructor
public class UserListController {

    private final UserService userService;

    @GetMapping
    public String getAllUsers(Model model) {
        model.addAttribute("users", userService.getAllUser());
        model.addAttribute("roles", RoleStub.values());
        return "admin/users-table";
    }

    @PostMapping("/{id}/delete")
    public String markAsDeleted(@PathVariable Long id) {
        userService.markUserAsDeleted(id);
        return "redirect:/admin/users-table";
    }
}
