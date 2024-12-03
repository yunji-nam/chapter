package com.example.chapter.controller;

import com.example.chapter.dto.ReviewRegistrationDto;
import com.example.chapter.dto.ReviewResponseDto;
import com.example.chapter.security.UserDetailsImpl;
import com.example.chapter.service.ReviewService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequiredArgsConstructor
public class ReviewController {

    private final ReviewService reviewService;

    // 리뷰 등록 폼
    @GetMapping("/book/{bookId}/review")
    public String addReviewForm(@PathVariable Long bookId, Model model) {
        model.addAttribute("form", new ReviewRegistrationDto());
        return "review/add";
    }

    // 리뷰 등록
    @PostMapping("/book/{bookId}/review")
    public String addReview(@PathVariable Long bookId,
                            @Valid ReviewRegistrationDto dto,
                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        reviewService.addReview(bookId, dto, userDetails.getUser());
        return "redirect:/book/" + bookId;
    }

    // 리뷰 조회
    @GetMapping("/book/{bookId}/review/{reviewId}")
    public String getReview(@PathVariable Long bookId, @PathVariable Long reviewId, Model model) {
        ReviewResponseDto dto = reviewService.getReview(reviewId);
        model.addAttribute("review", dto);

        return "review/detail";
    }

    // 리뷰 수정 폼
    @GetMapping("/book/{bookId}/review/{reviewId}/edit")
    public String updateReviewForm(@PathVariable Long bookId, @PathVariable Long reviewId, Model model) {
        ReviewResponseDto responseDto = reviewService.getReview(reviewId);
        model.addAttribute("form", new ReviewRegistrationDto(responseDto));
        return "review/update";
    }

    // 리뷰 수정
    @PutMapping("/book/{bookId}/review/{reviewId}/edit")
    public String updateReview(@PathVariable Long bookId, @PathVariable Long reviewId,
                             @Valid ReviewRegistrationDto dto,
                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        reviewService.updateReview(reviewId, dto, userDetails.getUser());
        return "redirect:/book/" + bookId + "/review/" + reviewId;
    }

    // 리뷰 삭제
    @DeleteMapping("/book/{bookId}/review/{reviewId}")
    public String deleteReview(@PathVariable Long bookId, @PathVariable Long reviewId,
                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        reviewService.deleteReview(reviewId, userDetails.getUser());
        return "redirect:/book/" + bookId;
    }

    @GetMapping("/reviews")
    public String getAllMyReviews(@AuthenticationPrincipal UserDetailsImpl userDetails, Model model) {
        List<ReviewResponseDto> MyReviews = reviewService.getAllMyReviews(userDetails.getUser());
        model.addAttribute("reviews", MyReviews);
        return "review/list";
    }
}
