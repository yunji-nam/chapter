package com.example.chapter.service;

import com.example.chapter.dto.ReviewRegistrationDto;
import com.example.chapter.dto.ReviewResponseDto;
import com.example.chapter.entity.Book;
import com.example.chapter.entity.Review;
import com.example.chapter.entity.User;
import com.example.chapter.repository.BookRepository;
import com.example.chapter.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    public void addReview(Long bookId, User user, ReviewRegistrationDto dto) {
        Book book = bookRepository.findById(bookId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 책입니다."));
        String content = dto.getContent();
        int rating = dto.getRating();

        Review review = new Review(content, rating, book, user);
        reviewRepository.save(review);
    }

    public void updateReview(Long reviewId, ReviewRegistrationDto dto) {
        Review review = reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));
        String content = dto.getContent();
        int rating = dto.getRating();

        review.updateReview(content, rating);
        reviewRepository.save(review);
    }

    public List<ReviewResponseDto> getReviews(Long bookId) {
        List<Review> reviews = reviewRepository.findByBookId(bookId);
        return reviews.stream().map(review -> new ReviewResponseDto(
                review.getId(),
                review.getUser().getName(),
                review.getContent(),
                review.getRating(),
                review.getCreatedDate(),
                review.getModifiedDate())).collect(Collectors.toList());
    }
}
