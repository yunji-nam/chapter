package com.example.chapter.repository;

import com.example.chapter.entity.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByBookId(Long bookId, Pageable pageable);
    List<Review> findAllByUserId(Long userId);
    boolean existsByBookIdAndUserId(Long bookId, Long userId);
}
