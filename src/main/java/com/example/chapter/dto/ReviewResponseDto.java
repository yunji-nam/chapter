package com.example.chapter.dto;

import com.example.chapter.entity.Review;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponseDto {

    private Long id;
    private Long bookId;
    private String bookTitle;
    private String username;
    private String content;
    private int rating;
    private LocalDateTime createdDate;
    private LocalDateTime modifiedDate;

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.bookId = review.getOrderItem().getBook().getId();
        this.bookTitle = review.getOrderItem().getBook().getTitle();
        this.username = review.getUser().getName();
        this.content = review.getContent();
        this.rating = review.getRating();
        this.createdDate = review.getCreatedAt();
        this.modifiedDate = review.getModifiedAt();
    }
}
