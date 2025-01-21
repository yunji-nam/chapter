package com.example.chapter.repository;

import com.example.chapter.entity.Like;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByBookIdAndUserId(Long bookId, Long userId);

    void deleteByBookIdAndUserId(Long bookId, Long userId);

    Page<Like> findAllByUserIdAndBookDeletedFalse(Long userId, Pageable pageable);

    List<Like> findTop8ByUserIdAndBookDeletedFalseOrderByCreatedAtDesc(Long userId);
}
