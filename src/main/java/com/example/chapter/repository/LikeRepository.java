package com.example.chapter.repository;

import com.example.chapter.entity.Like;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface LikeRepository extends JpaRepository<Like, Long> {
    boolean existsByBookIdAndUserId(Long bookId, Long userId);

    void deleteByBookIdAndUserId(Long bookId, Long userId);

    List<Like> findAllByUserId(Long userId);
}
