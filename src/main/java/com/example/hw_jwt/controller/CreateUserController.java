package com.example.hw_jwt.controller;

import com.example.hw_jwt.model.UserCreationResult;
import com.example.hw_jwt.service.UserService;
import com.example.hw_jwt.view.CreateUserView;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequestMapping("/create-user")
@RequiredArgsConstructor
public class CreateUserController {

    /**
     * Сервис по работе с пользователями.
     */
    private final UserService userService;

    @GetMapping()
    public String editLinkPage(Model model) {
        model.addAttribute("user", new CreateUserView());
        return "create-user";
    }

    @PostMapping()
    public String createUser(
            @ModelAttribute("user") CreateUserView userView,
            @RequestParam("action") String action,
            Model model
    ) {
        if ("create".equals(action)) {
            UserCreationResult result = userService.createUser(userView);

            if (result.success()) {
                String message = String.format("Пользователь '%s' создан.",userView.getLogin());
                model.addAttribute("message", message);
            } else {
                String message = String.format(
                        "Пользователь '%s' не создан, причина: '%s'.",
                        userView.getLogin(),
                        result.errorMessage()
                );
                model.addAttribute("errorMessage", message);
            }
        } else {
            model.addAttribute("errorMessage", "Неизвестная операция");
        }
        return "create-user";
    }

}
