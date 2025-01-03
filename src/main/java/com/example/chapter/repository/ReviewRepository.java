package com.example.chapter.repository;

import com.example.chapter.dto.ReviewResponseDto;
import com.example.chapter.entity.Review;
import org.jetbrains.annotations.NotNull;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r JOIN r.orderItem oi WHERE oi.book.id = :bookId")
    Page<ReviewResponseDto> findAllByBookId(@Param("bookId") Long bookId, Pageable pageable);

    @Query("SELECT r FROM Review r JOIN r.orderItem oi WHERE oi.book.id = :bookId")
    List<ReviewResponseDto> findAllByBookId(Long bookId);

    Page<Review> findAllByUserIdOrderByCreatedAt(Long userId, Pageable pageable);

    @NotNull Page<Review> findAll(@NotNull Pageable pageable);

    boolean existsByOrderItemIdAndUserId(Long orderItemId, Long userId);
}
