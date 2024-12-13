package com.example.chapter.controller.api;

import com.example.chapter.dto.ReviewResponseDto;
import com.example.chapter.service.ReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

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

}
