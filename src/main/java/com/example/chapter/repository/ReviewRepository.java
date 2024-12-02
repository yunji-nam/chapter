package com.example.chapter.repository;

import com.example.chapter.entity.Review;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    List<Review> findAllByOrderByModifiedDateDesc();
    List<Review> findAllByUserId(Long userId);
}
