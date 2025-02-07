package com.example.chapter.controller;

import com.example.chapter.dto.*;
import com.example.chapter.security.UserDetailsImpl;
import com.example.chapter.service.KakaoService;
import com.example.chapter.service.LikeService;
import com.example.chapter.service.OrderService;
import com.example.chapter.service.UserService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpSession;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@Controller
@Slf4j
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final KakaoService kakaoService;
    private final OrderService orderService;
    private final LikeService likeService;

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
    public String callback(@RequestParam String code, HttpSession session) throws JsonProcessingException {
        kakaoService.login(code, session);

        return "redirect:/";
    }

    @GetMapping("/join")
    public String signUpForm(Model model) {
        model.addAttribute("signUpDto", new SignUpDto());
        return "user/join";
    }

    @PostMapping("/join")
    public String signUp(@Valid SignUpDto signUpDto, BindingResult result) {
        if (result.hasErrors()) {
            return "user/join";
        }
        userService.join(signUpDto);
        return "redirect:/login";
    }

    @GetMapping("/user/me")
    public String getMyPage(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        List<OrderSummaryDto> orderList = orderService.getLatestOrderList(userDetails.getUser());
        List<BookListDto> likeList = likeService.getLatestLikeList(userDetails.getUser());
        model.addAttribute("orderList", orderList);
        model.addAttribute("likeList", likeList);
        return "user/myPage";
    }

    @GetMapping("/user/profile")
    public String getProfile(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        ProfileDto profile = userService.getProfile(userDetails.getUser());
        model.addAttribute("profile", profile);
        return "user/profile/view";
    }

    @GetMapping("/user/profile/edit")
    public String updateProfileForm(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        ProfileDto profileDto = userService.getProfile(userDetails.getUser());
        UpdateProfileDto profile = new UpdateProfileDto(profileDto);
        model.addAttribute("updateProfileDto", profile);

        return "user/profile/edit";
    }

    @PutMapping("/user/profile/edit")
    public String updateProfile(@AuthenticationPrincipal UserDetailsImpl userDetails,
                                @Valid UpdateProfileDto updateProfileDto, BindingResult result) {
        if (result.hasErrors()) {
            return "user/profile/edit";
        }
        userService.updateProfile(userDetails.getUser(), updateProfileDto);
        return "redirect:/user/profile";
    }

    @GetMapping("/user/password")
    public String updatePasswordForm(Model model) {
        model.addAttribute("passwordDto", new PasswordDto());
        return "user/profile/editPassword";
    }

    @PutMapping("/user/password")
    public String updatePassword(@AuthenticationPrincipal UserDetailsImpl userDetails, @Valid PasswordDto passwordDto, BindingResult result) {
        if (result.hasErrors()) {
            return "user/profile/editPassword";
        }
        userService.updatePassword(userDetails.getUser(), passwordDto);
        return "redirect:/user/profile";
    }

}