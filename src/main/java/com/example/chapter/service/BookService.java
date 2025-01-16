package com.example.chapter.service;

import com.example.chapter.dto.*;
import com.example.chapter.entity.Book;
import com.example.chapter.entity.Category;
import com.example.chapter.repository.BookRepository;
import com.example.chapter.repository.ReviewRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

import static com.example.chapter.constant.CacheConstant.BOOK_LIST;
import static com.example.chapter.constant.CacheConstant.FEATURED_BOOK_LIST;

@Service
@Slf4j
@RequiredArgsConstructor
public class BookService {

    private final BookRepository bookRepository;
    private final ReviewRepository reviewRepository;
    private final S3Service s3Service;

    // 도서 등록
    @CacheEvict(cacheNames = {BOOK_LIST, FEATURED_BOOK_LIST}, allEntries = true)
    public void registerBook(BookRegistrationDto dto) {
        MultipartFile image = dto.getImage();
        if (image == null || image.isEmpty()) {
            throw new IllegalArgumentException("file이 비어 있습니다.");
        }
        String imageUrl = s3Service.upload(image);
        Book book = dto.toEntity(imageUrl);
        bookRepository.save(book);
    }

    // 도서 모두 조회
    @Cacheable(cacheNames = BOOK_LIST, key = "'page:' + #p1")
    public Page<BookListDto> getBooks(String sortType, int pageNo, int size) {
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(Sort.Direction.DESC, sortType));
        Page<Book> page = bookRepository.findAllByDeletedFalse(pageable);
        return new CustomPageImpl<>(page.map(BookListDto::new));
    }

    // 카테고리 별 조회
    @Cacheable(cacheNames = BOOK_LIST, key = "'category:' + #p0 + 'page:' + #p2")
    public Page<BookListDto> getBooksByCategory(Category category, String sortType, int pageNo, int size) {
        Pageable pageable = PageRequest.of(pageNo, size, Sort.by(Sort.Direction.DESC, sortType));
        Page<Book> page = bookRepository.findByCategoryAndDeletedFalse(category, pageable);
        return new CustomPageImpl<>(page.map(BookListDto::new));
    }

    // 주간 베스트 도서 조회
    @Cacheable(cacheNames = FEATURED_BOOK_LIST, key = "'best:' + #pageable.pageNumber")
    public Page<BookListDto> getBooksByBestSelling(Pageable pageable) {
        LocalDateTime oneWeekAgo = LocalDateTime.now().minusDays(7);
        Page<BookListDto> bestSellingBooks = bookRepository.findBestSellingBooks(oneWeekAgo, pageable);
        return new CustomPageImpl<>(bestSellingBooks);
    }

    // 최신(30일 내) 등록 도서 조회
    @Cacheable(cacheNames = FEATURED_BOOK_LIST, key = "'new:' + #pageable.pageNumber")
    public Page<BookListDto> getBooksByLatest(Pageable pageable) {
        LocalDateTime thirtyDaysAgo = LocalDateTime.now().minusDays(30);
        LocalDateTime now = LocalDateTime.now();
        Page<Book> page = bookRepository.findByCreatedAtBetweenAndDeletedFalseOrderByIdDesc(thirtyDaysAgo, now, pageable);
        return new CustomPageImpl<>(page.map(BookListDto::new));
    }

    // 도서 상세 조회
    public BookDetailDto getBook(Long id) {
        Book book = findBook(id);
        List<ReviewResponseDto> reviewResponseDto = reviewRepository.findAllByBookId(id);
        return new BookDetailDto(book, reviewResponseDto);
    }

    // 도서 수정
    @Transactional
    @CacheEvict(cacheNames = {BOOK_LIST, FEATURED_BOOK_LIST}, allEntries = true)
    public void updateBook(Long id, BookRegistrationDto dto) {
        Book book = findBook(id);
        book.update(dto);
    }

    // 도서 이미지 변경
    @Transactional
    @CacheEvict(cacheNames = {BOOK_LIST, FEATURED_BOOK_LIST}, allEntries = true)
    public String updateBookImage(Long bookId, MultipartFile file) {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("file이 비어 있습니다.");
        }
        Book book = findBook(bookId);
        String imageUrl = book.getImage();
        String newImageUrl = s3Service.upload(file);
        book.updateImage(newImageUrl);
        s3Service.delete(imageUrl);
        return newImageUrl;
    }

    // 도서 삭제
    @Transactional
    @CacheEvict(cacheNames = {BOOK_LIST, FEATURED_BOOK_LIST}, allEntries = true)
    public void deleteBooks(List<Long> ids) {
        List<Book> books = bookRepository.findAllById(ids);
        books.forEach(Book::delete);
    }

    // 도서 상태변경
    @Transactional
    @CacheEvict(cacheNames = {BOOK_LIST, FEATURED_BOOK_LIST}, allEntries = true)
    public void updateBookStatus(BookStatusUpdateDto dto) {
        List<Book> books = bookRepository.findAllById(dto.getBookIds());
        books.forEach(book -> book.updateStatus(dto.getStatus()));
    }

    // 도서 검색
    public Page<BookListDto> searchBook(String keyword, Pageable pageable) {
        PageRequest pageRequest = PageRequest.of(pageable.getPageNumber(), pageable.getPageSize(), Sort.by(Sort.Direction.DESC, "createdAt"));
        return bookRepository.findAllByKeywordIgnoreCaseAndDeletedFalse(keyword, pageRequest);
    }

    public Page<BookListDto> searchBookByCondition(String keyword, String condition, Pageable pageable) {
        Page<Book> page;
        switch (condition.toLowerCase()) {
            case "title":
                page = bookRepository.findByTitleContainingIgnoreCaseAndDeletedFalse(keyword, pageable);
                break;
            case "author":
                page = bookRepository.findByAuthorContainingIgnoreCaseAndDeletedFalse(keyword, pageable);
                break;
            case "publisher":
                page = bookRepository.findByPublisherContainingIgnoreCaseAndDeletedFalse(keyword, pageable);
                break;
            case "isbn":
                page = bookRepository.findByIsbnContainingIgnoreCaseAndDeletedFalse(keyword, pageable);
                break;
            default:
                throw new IllegalArgumentException("유효하지 않은 값입니다.");
        }
        return page.map(BookListDto::new);
    }

    private Book findBook(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("책을 찾을 수 없습니다."));
    }

}
