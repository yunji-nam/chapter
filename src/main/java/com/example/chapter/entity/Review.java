package com.example.chapter.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Review {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String content;

    @Column(nullable = false)
    private int rating;

    @Column(nullable = false)
    private LocalDate createdDate;

    private LocalDate modifiedDate;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id", nullable = false)
    private Book book;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    public Review(String content, int rating, Book book, User user) {
        this.content = content;
        this.rating = rating;
        this.createdDate = LocalDate.now();
        this.modifiedDate = LocalDate.now();
        this.book = book;
        this.user = user;
    }

    public void updateReview(String content, int rating) {
        this.content = content;
        this.rating = rating;
        this.modifiedDate = LocalDate.now();
    }
}
