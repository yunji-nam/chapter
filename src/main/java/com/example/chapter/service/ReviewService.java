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

@Service
@RequiredArgsConstructor
@Slf4j
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;

    public void addReview(Long orderItemId, ReviewRegistrationDto dto, User user) {
        OrderItem orderItem = orderItemRepository.findById(orderItemId).orElseThrow(() -> new EntityNotFoundException("주문한 책을 찾을 수 없습니다."));
        Order order = orderRepository.findByItemsId(orderItemId).orElseThrow(() -> new EntityNotFoundException("주문을 찾을 수 없습니다."));
        ReviewStatus reviewStatus = getReviewStatus(order.getId(), user, orderItemId);
        if (!(reviewStatus == ReviewStatus.CAN_WRITE)) {
            throw new IllegalArgumentException("잘못된 접근입니다.");
        }

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

    // 하나의 책에 대한 리뷰 리스트
    public Page<ReviewResponseDto> getBookReviews(Long bookId, int pageNo, int size) {
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(Sort.Direction.DESC, "modifiedAt"));
        return reviewRepository.findAllByBookIdAndDeletedFalse(bookId, pageable);
    }

    // 리뷰 목록 조회
    public Page<ReviewResponseDto> getReviews(User user, Pageable pageable) {
        Page<Review> reviews = reviewRepository.findAllByUserIdAndDeletedFalseOrderByCreatedAt(user.getId(), pageable);
        if (user.isAdmin()) {
            reviews = reviewRepository.findAllByDeletedFalse(pageable);
        }
        return reviews.map(ReviewResponseDto::new);
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
        review.delete();
    }

    private Review findReview(Long reviewId) {
        return reviewRepository.findByIdAndDeletedFalse(reviewId)
                .orElseThrow(() -> new EntityNotFoundException("리뷰를 찾을 수 없습니다"));
    }

    private static void checkUser(User user, Review review) {
        if (!user.getId().equals(review.getUser().getId()) && !user.isAdmin()) {
            throw new IllegalArgumentException("권한이 없습니다.");
        }
    }
}
