package com.example.chapter.controller.api;

import com.example.chapter.dto.ApiResponse;
import com.example.chapter.security.UserDetailsImpl;
import com.example.chapter.service.LikeService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/book/{bookId}")
public class LikeApiController {

    private final LikeService likeService;

    @PostMapping("/like")
    public ApiResponse<String> likeBook(@PathVariable Long bookId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        likeService.likeBook(bookId, userDetails.getUser());
        return new ApiResponse<>("좋아요 완료");
    }

    @DeleteMapping("/like")
    public ApiResponse<String> unlikeBook(@PathVariable Long bookId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        likeService.unlikeBook(bookId, userDetails.getUser());
        return new ApiResponse<>("좋아요 취소 완료");
    }

}
