package com.example.chapter.repository;

import com.example.chapter.dto.ReviewResponseDto;
import com.example.chapter.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r JOIN r.orderItem oi WHERE oi.book.id = :bookId AND r.deleted = false")
    Page<ReviewResponseDto> findAllByBookIdAndDeletedFalse(@Param("bookId") Long bookId, Pageable pageable);

    Page<Review> findAllByUserIdAndDeletedFalseOrderByCreatedAt(Long userId, Pageable pageable);

    Page<Review> findAllByDeletedFalse(Pageable pageable);

    boolean existsByOrderItemIdAndUserId(Long orderItemId, Long userId);

    Optional<Review> findByIdAndDeletedFalse(Long id);

}
