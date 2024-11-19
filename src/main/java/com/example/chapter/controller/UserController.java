package com.example.chapter.controller;

import com.example.chapter.dto.ProfileDto;
import com.example.chapter.dto.SignUpDto;
import com.example.chapter.dto.UpdateProfileDto;
import com.example.chapter.entity.User;
import com.example.chapter.security.UserDetailsImpl;
import com.example.chapter.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

@Controller
@Slf4j
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
    public String signUp(@Valid SignUpDto form, BindingResult result) {
        if (result.hasErrors()) {
            return "user/join";
        }
        userService.join(form);
        return "redirect:/login";
    }

    @GetMapping("/profile")
    public String getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        ProfileDto profile = userService.getProfile(userDetails.getUser());
        model.addAttribute("profile", profile);
        return "user/profile/view";
    }

    @GetMapping("/profile/edit")
    public String updateProfileForm(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        User user = userDetails.getUser();
        UpdateProfileDto profile = new UpdateProfileDto(user.getPassword(), user.getEmail(),
                user.getPhone(), user.getAddress());
        model.addAttribute("form", profile);

        return "user/profile/edit";
    }

    @PutMapping("/profile/edit")
    public String updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                @Valid UpdateProfileDto dto) {
        userService.updateProfile(userDetails.getUser(), dto);
        return "redirect:/profile";
    }

}