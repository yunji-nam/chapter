package com.example.chapter.controller;

import com.example.chapter.dto.SignUpDto;
import com.example.chapter.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/join")
    public String signUpForm(Model model) {
        model.addAttribute("form", new SignUpDto());
        return "admin/join";
    }

    @PostMapping("/join")
    public String signUp(@Valid SignUpDto form, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/join";
        }
        adminService.join(form);
        return "redirect:/login";
    }

}
