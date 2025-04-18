package com.example.hw_jwt.controller;

import com.example.hw_jwt.entity.RoleType;
import com.example.hw_jwt.model.UserLoginResult;
import com.example.hw_jwt.service.UserService;
import com.example.hw_jwt.view.LoginUserView;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Slf4j
@Controller
@RequestMapping("/login")
@RequiredArgsConstructor
public class LoginController {

    private final UserService userService;

    @GetMapping
    public String loginPage(Model model) {
        model.addAttribute("user", new LoginUserView());
        return "login";
    }

    @PostMapping
    public String login(@ModelAttribute LoginUserView user,
                        HttpServletResponse response,
                        Model model) {
        log.info("login user: {}", user.getLogin());
        UserLoginResult userLoginResult = userService.loginUser(user);
        if (userLoginResult.success()) {
            Cookie cookie = new Cookie("JWT", userLoginResult.token());
            cookie.setHttpOnly(true);
            cookie.setPath("/");
            response.addCookie(cookie);
            return userLoginResult.roleType().equals(RoleType.ADMIN)
                    ? "redirect:/admin/operations"
                    : "redirect:/user/operations";
        } else {
            model.addAttribute("errorMessage", userLoginResult.errorMessage());
            return "redirect:/login";
        }
    }
}
