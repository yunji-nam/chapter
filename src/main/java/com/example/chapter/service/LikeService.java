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
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

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

    public List<BookListDto> getLikeList(User user) {
        List<Like> likeList = likeRepository.findAllByUserId(user.getId());
        List<Book> bookList = likeList.stream().map(Like::getBook).toList();
        return bookList.stream().map(BookListDto::new).toList();
    }

    public boolean getLikeStatus(User user, Long bookId) {
        return likeRepository.existsByBookIdAndUserId(bookId, user.getId());
    }

    private Book getBookById(Long bookId) {
        return bookRepository.findById(bookId).orElseThrow(() ->
                new EntityNotFoundException("book을 찾을 수 없습니다."));
    }

}
