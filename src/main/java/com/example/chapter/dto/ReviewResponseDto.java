package com.example.chapter.dto;

import com.example.chapter.entity.Review;
import lombok.Getter;

import java.time.LocalDate;

@Getter
public class ReviewResponseDto {

    private Long id;
    private Long bookId;
    private String bookTitle;
    private String username;
    private String content;
    private int rating;
    private LocalDate modifiedDate;

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.bookId = review.getBook().getId();
        this.bookTitle = review.getBook().getTitle();
        this.username = review.getUser().getName();
        this.content = review.getContent();
        this.rating = review.getRating();
        this.modifiedDate = review.getModifiedDate();
    }
}
