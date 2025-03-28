package com.example.chapter.dto;

import com.example.chapter.entity.Review;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;

@Getter
public class ReviewResponseDto {

    private Long id;
    private Long bookId;
    private String bookTitle;
    private String bookAuthor;
    private String username;
    private String content;
    private int rating;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime createdDate;
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDateTime modifiedDate;

    public ReviewResponseDto(Review review) {
        this.id = review.getId();
        this.bookId = review.getOrderItem().getBook().getId();
        this.bookTitle = review.getOrderItem().getBook().getTitle();
        this.bookAuthor = review.getOrderItem().getBook().getAuthor();
        this.username = review.getUser().getName();
        this.content = review.getContent();
        this.rating = review.getRating();
        this.createdDate = review.getCreatedAt();
        this.modifiedDate = review.getModifiedAt();
    }
}
