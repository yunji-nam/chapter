package com.example.chapter.controller;

import com.example.chapter.dto.SignUpDto;
import com.example.chapter.dto.UserInfo;
import com.example.chapter.service.AdminService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/admin")
public class AdminController {

    private final AdminService adminService;

    @GetMapping("/join")
    public String signUpForm(Model model) {
        model.addAttribute("signUpDto", new SignUpDto());
        return "admin/join";
    }

    @PostMapping("/join")
    public String signUp(@Valid SignUpDto signUpDto, BindingResult result) {
        if (result.hasErrors()) {
            return "admin/join";
        }
        adminService.join(signUpDto);
        return "redirect:/login";
    }

    @GetMapping("/users")
    public String getUsers(@PageableDefault(size = 20) Pageable pageable, Model model) {
        Page<UserInfo> users = adminService.getUsers(pageable);
        model.addAttribute("userList", users);

        return "admin/user/list";
    }

    @GetMapping("/user/search")
    public String searchUser(@RequestParam String query,
                             @RequestParam String condition,
                             @PageableDefault(size = 20) Pageable pageable, Model model) {
        Page<UserInfo> users = adminService.searchUser(query, condition, pageable);
        model.addAttribute("userList", users);
        model.addAttribute("query", query);
        model.addAttribute("condition", condition);

        return "admin/user/search";
    }

}
