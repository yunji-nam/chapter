package com.example.chapter.controller;

import com.example.chapter.dto.ProfileDto;
import com.example.chapter.dto.SignUpDto;
import com.example.chapter.dto.UpdateProfileDto;
import com.example.chapter.entity.User;
import com.example.chapter.security.UserDetailsImpl;
import com.example.chapter.service.KakaoService;
import com.example.chapter.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
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
    private final KakaoService kakaoService;

    @Value("${kakao.restapi.key}")
    private String clientId;

    @Value("${kakao.redirect.uri}")
    private String redirectUri;

    @GetMapping("/login")
    public String login(Model model) {
        String location = "https://kauth.kakao.com/oauth/authorize?response_type=code&client_id=" + clientId + "&redirect_uri=" + redirectUri;
        model.addAttribute("location", location);
        return "user/login";
    }

    @GetMapping("/kakao/callback")
    public String callback(@RequestParam String code) throws JsonProcessingException {
        kakaoService.login(code);
        return "redirect:/";
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