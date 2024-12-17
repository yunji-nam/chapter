package com.example.chapter.service;

import com.example.chapter.dto.BookDetailDto;
import com.example.chapter.dto.BookListDto;
import com.example.chapter.dto.BookRegistrationDto;
import com.example.chapter.dto.ReviewResponseDto;
import com.example.chapter.entity.Book;
import com.example.chapter.entity.Category;
import com.example.chapter.entity.Review;
import com.example.chapter.entity.User;
import com.example.chapter.repository.BookRepository;
import com.example.chapter.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
//    private final S3Service s3Service;

    // 도서 등록
    public void registerBook(BookRegistrationDto dto, User user) {
        checkAuthority(user);
        String imageUrl = "url";

        Book book = dto.toEntity(imageUrl);
        bookRepository.save(book);
    }


    // 도서 모두 조회
    public Page<BookListDto> getBooks(String sortType, int pageNo, int size) {
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(Sort.Direction.DESC, sortType));
        Page<Book> page = bookRepository.findAll(pageable);

        return page.map(BookListDto::new);
    }

    // 카테고리 별 조회
    public Page<BookListDto> getBooksByCategory(Category category, String sortType, int pageNo, int size) {
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(Sort.Direction.DESC, sortType));
        Page<Book> page = bookRepository.findByCategory(category, pageable);

        return page.map(BookListDto::new);
    }

    // 도서 상세 조회
    public BookDetailDto getBook(Long id) {
        Book book = findBook(id);
        List<Review> reviews = reviewRepository.findAllByBookId(id);
        List<ReviewResponseDto> reviewResponseDto = reviews.stream().map(ReviewResponseDto::new).toList();
        return new BookDetailDto(book, reviewResponseDto);
    }

    // 도서 수정
    @Transactional
    public void updateBook(Long id, BookRegistrationDto dto, User user) {
        Book book = findBook(id);
        checkAuthority(user);

        book.update(dto.toEntity("url"));
    }

    // 도서 삭제
    @Transactional
    public void deleteBook(Long id, User user) {
        Book book = findBook(id);
        checkAuthority(user);
        bookRepository.delete(book);
    }

    // 도서 검색
    private Book findBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("책을 찾을 수 없습니다."));
    }

    private static void checkAuthority(User user) {
        if (!user.isAdmin()) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }
    }

    public Page<BookListDto> searchBook(String keyword, Pageable pageable) {
        return bookRepository.findAllByKeywordContainsIgnoreCase(keyword, pageable);
    }
}
