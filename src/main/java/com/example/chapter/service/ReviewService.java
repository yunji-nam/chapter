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
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final BookRepository bookRepository;

    public void addReview(Long id, ReviewRegistrationDto dto, User user) {
        Book book = bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 책입니다."));
        String content = dto.getContent();
        int rating = dto.getRating();

        Review review = new Review(content, rating, book, user);
        reviewRepository.save(review);
    }

    public List<ReviewResponseDto> getReviews() {
        return reviewRepository.findAllByOrderByModifiedDateDesc().stream().map(ReviewResponseDto::new).toList();
    }

    public ReviewResponseDto getReview(Long id) {
        Review review = findReview(id);
        return new ReviewResponseDto(review);
    }

    @Transactional
    public void updateReview(Long id, ReviewRegistrationDto dto, User user) {
        Review review = findReview(id);
        if (!user.getId().equals(review.getUser().getId())) {
            throw new IllegalArgumentException("작성자만 수정할 수 있습니다.");
        }
        String content = dto.getContent();
        int rating = dto.getRating();

        review.update(content, rating);
    }

    @Transactional
    public void deleteReview(Long id, User user) {
        Review review = findReview(id);
        if (!user.getId().equals(review.getUser().getId())) {
            throw new IllegalArgumentException("작성자만 삭제할 수 있습니다.");
        }
        reviewRepository.delete(review);
    }

    private Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 리뷰입니다."));
    }
}
