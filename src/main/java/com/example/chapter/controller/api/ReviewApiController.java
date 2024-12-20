package com.example.chapter.controller.api;

import com.example.chapter.dto.ApiResponse;
import com.example.chapter.dto.ReviewResponseDto;
import com.example.chapter.security.UserDetailsImpl;
import com.example.chapter.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
public class ReviewApiController {

    private final ReviewService reviewService;

    @GetMapping("/api/book/{bookId}/reviews")
    public Page<ReviewResponseDto> getReviews(@PathVariable Long bookId,
                                              @RequestParam(defaultValue = "0") int pageNo,
                                              @RequestParam(defaultValue = "5") int size) {
        return reviewService.getReviews(bookId, pageNo, size);
    }

    @DeleteMapping("/api/book/{bookId}/review/{reviewId}")
    public ApiResponse<String> deleteReview(@PathVariable Long bookId, @PathVariable Long reviewId,
                                            @AuthenticationPrincipal UserDetailsImpl userDetails) {
        reviewService.deleteReview(reviewId, userDetails.getUser());
        return new ApiResponse<>("review 삭제 완료");
    }

}
