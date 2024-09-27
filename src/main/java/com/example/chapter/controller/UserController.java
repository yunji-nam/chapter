package com.example.chapter.controller;

import com.example.chapter.dto.SignUpDto;
import com.example.chapter.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api")
public class UserController {

    private final UserService userService;

    @PostMapping("/member/new")
    public ResponseEntity<String> signUp(@Valid @RequestBody SignUpDto signUpDto) {
        userService.join(signUpDto);
        return ResponseEntity.ok().body("회원가입 성공");
    }
}
