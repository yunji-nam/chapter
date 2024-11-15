package com.example.chapter.controller.api;

import com.example.chapter.service.KakaoService;
import com.fasterxml.jackson.core.JsonProcessingException;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
@RequestMapping("/kakao")
public class KakaoController {

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

    @GetMapping("/callback")
    public String callback(@RequestParam String code, HttpServletResponse response) throws JsonProcessingException {
        kakaoService.login(code, response);
        return "redirect:/";
    }

}
