package com.example.chapter.service;

import com.example.chapter.dto.BookRegistrationDto;
import com.example.chapter.dto.BookResponseDto;
import com.example.chapter.entity.*;
import com.example.chapter.repository.BookRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;

@Service
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
//    private final S3Service s3Service;

    /*
     * 도서 등록
     */
    public void addBook(BookRegistrationDto dto, MultipartFile image, User user) {
        checkAuthority(user);

        String title = dto.getTitle();
        String author = dto.getAuthor();
        String publisher = dto.getPublisher();
        String isbn = dto.getIsbn();
        int pages = dto.getPages();
        Category category = dto.getCategory();
        LocalDate publishedDate = dto.getPublishedDate();
        int price = dto.getPrice();
        String description = dto.getDescription();
        int quantity = dto.getQuantity();
//        String imageUrl = s3Service.upload();

        Book book = new Book(title, author, publisher, isbn, pages, category,
                publishedDate, price, description, quantity);

        bookRepository.save(book);
    }

    /*
     * 도서 모두 조회
     */
    public Page<BookResponseDto> getAllBooks(String sortType, int pageNo, int size) {
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(Sort.Direction.DESC, sortType));
        Page<Book> page = bookRepository.findAll(pageable);

        return page.map(BookResponseDto::new);
    }

    public Page<BookResponseDto> getBooksByCategory(Category category, String sortType, int pageNo, int size) {
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(Sort.Direction.DESC, sortType));
        Page<Book> page = bookRepository.findByCategory(category, pageable);

        return page.map(BookResponseDto::new);
    }

    public BookResponseDto getBook(Long id) {
        Book book = findBook(id);
        return new BookResponseDto(book);
    }

    @Transactional
    public void updateBook(Long id, BookRegistrationDto dto, User user) {
        Book book = findBook(id);
        checkAuthority(user);

        String title = dto.getTitle();
        String author = dto.getAuthor();
        String publisher = dto.getPublisher();
        String isbn = dto.getIsbn();
        int pages = dto.getPages();
        Category category = dto.getCategory();
        LocalDate publishedDate = dto.getPublishedDate();
        int price = dto.getPrice();
        String description = dto.getDescription();
        int quantity = dto.getQuantity();

        book.update(title, author, publisher, isbn, pages, category, publishedDate, price, description, quantity);
    }

    @Transactional
    public void deleteBook(Long id, User user) {
        Book book = findBook(id);
        checkAuthority(user);
        bookRepository.delete(book);
    }

    private Book findBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("존재하지 않는 책입니다."));
    }

    private static void checkAuthority(User user) {
        if (!user.isAdmin()) {
            throw new IllegalArgumentException("접근 권한이 없습니다.");
        }
    }

}
