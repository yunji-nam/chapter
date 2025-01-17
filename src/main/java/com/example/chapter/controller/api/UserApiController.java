package com.example.chapter.controller.api;

import com.example.chapter.dto.api.ApiResponse;
import com.example.chapter.security.UserDetailsImpl;
import com.example.chapter.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class UserApiController {

    private final UserService userService;

    //회원 탈퇴
    @PutMapping("/user/{userId}/withdraw")
    public ApiResponse<String> withdraw(@PathVariable Long userId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        userService.withdraw(userId, userDetails.getUser());
        return new ApiResponse<>("회원 탈퇴 완료");
    }
}
