package com.example.chapter.service;

import com.example.chapter.dto.ReviewRegistrationDto;
import com.example.chapter.dto.ReviewResponseDto;
import com.example.chapter.entity.*;
import com.example.chapter.repository.OrderItemRepository;
import com.example.chapter.repository.OrderRepository;
import com.example.chapter.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public void addReview(Long orderItemId, ReviewRegistrationDto dto, User user) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(() -> new EntityNotFoundException("주문한 책을 찾을 수 없습니다."));

        String content = dto.getContent();
        int rating = dto.getRating();

        Review review = new Review(content, rating, orderItem, user);
        reviewRepository.save(review);
    }

    public ReviewStatus getReviewStatus(Long orderId, User user, Long orderItemId) {
        boolean checkOrder = orderRepository.existsByIdAndUserIdAndItemsIdAndStatusAndDeliveryStatus(orderId, user.getId(), orderItemId, OrderStatus.PAID, DeliveryStatus.DELIVERED);
        boolean hasReview = reviewRepository.existsByOrderItemIdAndUserId(orderItemId, user.getId());

        if (checkOrder && !hasReview) {
            return ReviewStatus.CAN_WRITE;
        } else if (checkOrder && hasReview) {
            return ReviewStatus.COMPLETED;
        }
        return ReviewStatus.NOT_ALLOWED;
    }

    public Page<ReviewResponseDto> getReviews(Long bookId, int pageNo, int size) {
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(Sort.Direction.DESC, "modifiedAt"));
        return reviewRepository.findAllByBookId(bookId, pageable).map(ReviewResponseDto::new);
    }

    public List<ReviewResponseDto> getAllMyReviews(User user) {
        return reviewRepository.findAllByUserIdOrderByCreatedAt(user.getId()).stream().map(ReviewResponseDto::new).toList();
    }

    public ReviewResponseDto getReview(Long id) {
        Review review = findReview(id);
        return new ReviewResponseDto(review);
    }

    @Transactional
    public void updateReview(Long id, ReviewRegistrationDto dto, User user) {
        Review review = findReview(id);
        checkUser(user, review);

        String content = dto.getContent();
        int rating = dto.getRating();

        review.update(content, rating);
    }

    @Transactional
    public void deleteReview(Long id, User user) {
        Review review = findReview(id);
        checkUser(user, review);
        reviewRepository.delete(review);
    }

    private Review findReview(Long reviewId) {
        return reviewRepository.findById(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("리뷰를 찾을 수 없습니다"));
    }

    private static void checkUser(User user, Review review) {
        if (!user.getId().equals(review.getUser().getId()) && !user.isAdmin()) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }
}
