package com.example.chapter.service;

import com.example.chapter.entity.Book;
import com.example.chapter.entity.Like;
import com.example.chapter.entity.User;
import com.example.chapter.repository.BookRepository;
import com.example.chapter.repository.LikeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final BookRepository bookRepository;

    public void likeBook(Long bookId, User user) {
        Long userId = user.getId();
        Book book = getBookById(bookId);

        if (likeRepository.existsByBookIdAndUserId(bookId, userId)) {
            throw new IllegalArgumentException("이미 좋아요 한 책입니다.");
        } else {
            Like like = new Like(user, book);
            likeRepository.save(like);
        }
    }

    public void unlikeBook(Long bookId, User user) {
        Long userId = user.getId();

        if (likeRepository.existsByBookIdAndUserId(bookId, userId)) {
            likeRepository.deleteByBookIdAndUserId(bookId, userId);
        } else {
            throw new IllegalArgumentException("좋아요가 존재하지 않습니다.");
        }
    }

    private Book getBookById(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() ->
                new EntityNotFoundException("book을 찾을 수 없습니다."));
    }

}
