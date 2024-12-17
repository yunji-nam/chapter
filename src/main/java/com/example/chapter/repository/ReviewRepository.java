package com.example.chapter.repository;

import com.example.chapter.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    @Query("SELECT r FROM Review r JOIN r.orderItem oi WHERE oi.book.id = :bookId")
    Page<Review> findAllByBookId(@Param("bookId") Long bookId, Pageable pageable);

    @Query("SELECT r FROM Review r JOIN r.orderItem oi WHERE oi.book.id = :bookId")
    List<Review> findAllByBookId(Long bookId);

    List<Review> findAllByUserIdOrderByCreatedDate(Long userId);

    boolean existsByOrderItemIdAndUserId(Long orderItemId, Long userId);
}
