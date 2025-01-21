package com.example.chapter.service;

import com.example.chapter.dto.BookListDto;
import com.example.chapter.entity.Book;
import com.example.chapter.entity.Like;
import com.example.chapter.entity.User;
import com.example.chapter.repository.BookRepository;
import com.example.chapter.repository.LikeRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class LikeService {

    private final LikeRepository likeRepository;
    private final BookRepository bookRepository;

    public void likeBook(Long bookId, User user) {
        Book book = getBookById(bookId);
        Like like = new Like(user, book);
        likeRepository.save(like);
    }

    @Transactional
    public void unlikeBook(Long bookId, User user) {
        Long userId = user.getId();
        if (likeRepository.existsByBookIdAndUserId(bookId, userId)) {
            likeRepository.deleteByBookIdAndUserId(bookId, userId);
        }
    }

    public Page<BookListDto> getLikeList(User user, Pageable pageable) {
        Page<Like> likeList = likeRepository.findAllByUserIdAndBookDeletedFalse(user.getId(), pageable);
        return likeList.map(like -> new BookListDto(like.getBook()));
    }

    public List<BookListDto> getLikeList(User user) {
        List<Like> likeList = likeRepository.findTop8ByUserIdAndBookDeletedFalseOrderByCreatedAtDesc(user.getId());
        return likeList.stream().map(like -> new BookListDto(like.getBook())).collect(Collectors.toList());
    }

    public boolean getLikeStatus(User user, Long bookId) {
        return likeRepository.existsByBookIdAndUserId(bookId, user.getId());
    }

    private Book getBookById(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() ->
                new EntityNotFoundException("book을 찾을 수 없습니다."));
    }

}
