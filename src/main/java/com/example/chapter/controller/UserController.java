package com.example.chapter.controller;

import com.example.chapter.dto.SignUpDto;
import com.example.chapter.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/join")
    public String signUpForm(Model model) {
        model.addAttribute("form", new SignUpDto());
        return "user/join";
    }

    @PostMapping("/join")
    public String signUp(@Valid @RequestBody SignUpDto signUpDto, BindingResult result) {
        if (result.hasErrors()) {
            return "user/join";
        }
        userService.join(signUpDto);
        return "redirect:/login";
    }
}