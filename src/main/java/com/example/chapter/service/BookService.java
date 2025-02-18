package com.example.chapter.service;

import com.example.chapter.aop.RedissonLock;
import com.example.chapter.dto.*;
import com.example.chapter.entity.Book;
import com.example.chapter.entity.Category;
import com.example.chapter.repository.BookRepository;
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
    @Cacheable(cacheNames = BOOK_LIST, key = "'sort:' + #p0 + 'page:' + #p1")
    public Page<BookListDto> getBooks(String sortType, int pageNo, int size) {
        Pageable pageable = getPageable(pageNo, size, sortType);
        Page<Book> page = bookRepository.findAllByDeletedFalse(pageable);
        return new CustomPageImpl<>(page.map(BookListDto::new));
    }

    // 도서 모두 조회 - 어드민
    public Page<BookListDto> getBooksForAdmin(String sortType, int pageNo, int size) {
        Pageable pageable = getPageable(pageNo, size, sortType);
        Page<Book> page = bookRepository.findAllByDeletedFalse(pageable);
        return page.map(BookListDto::new);
    }

    // 카테고리 별 조회
    @Cacheable(cacheNames = BOOK_LIST, key = "'category:' + #p0 + 'sort:' + #p1 + 'page:' + #p2")
    public Page<BookListDto> getBooksByCategory(Category category, String sortType, int pageNo, int size) {
        Pageable pageable = getPageable(pageNo, size, sortType);
        Page<Book> page = bookRepository.findByCategoryAndDeletedFalse(category, pageable);
        return new CustomPageImpl<>(page.map(BookListDto::new));
    }

    public Page<BookListDto> getBooksByCategoryForAdmin(Category category, String sortType, int pageNo, int size) {
        Pageable pageable = getPageable(pageNo, size, sortType);
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
        return new BookDetailDto(book);
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

    public Page<BookListDto> searchBookByCondition(Category category, String sortType, String query, String condition, int pageNo, int size) {
        Pageable pageable = getPageable(pageNo, size, sortType);

        Page<Book> page;

        if (category != null) {
            switch (condition.toLowerCase()) {
                case "title":
                    page = bookRepository.findByCategoryAndTitleContainingIgnoreCaseAndDeletedFalse(category, query, pageable);
                    break;
                case "author":
                    page = bookRepository.findByCategoryAndAuthorContainingIgnoreCaseAndDeletedFalse(category, query, pageable);
                    break;
                case "publisher":
                    page = bookRepository.findByCategoryAndPublisherContainingIgnoreCaseAndDeletedFalse(category, query, pageable);
                    break;
                case "isbn":
                    page = bookRepository.findByCategoryAndIsbnContainingIgnoreCaseAndDeletedFalse(category, query, pageable);
                    break;
                default:
                    throw new IllegalArgumentException("유효하지 않은 값입니다.");
            }
        } else {
            switch (condition.toLowerCase()) {
                case "title":
                    page = bookRepository.findByTitleContainingIgnoreCaseAndDeletedFalse(query, pageable);
                    break;
                case "author":
                    page = bookRepository.findByAuthorContainingIgnoreCaseAndDeletedFalse(query, pageable);
                    break;
                case "publisher":
                    page = bookRepository.findByPublisherContainingIgnoreCaseAndDeletedFalse(query, pageable);
                    break;
                case "isbn":
                    page = bookRepository.findByIsbnContainingIgnoreCaseAndDeletedFalse(query, pageable);
                    break;
                default:
                    throw new IllegalArgumentException("유효하지 않은 값입니다.");
            }
        }
        return page.map(BookListDto::new);
    }

    @RedissonLock(value = "#book.id")
    public void decreaseStock(Book book, int quantity) {
        Book findBook = findBook(book.getId());
        findBook.decreaseStock(quantity);
    }

    public BookImageUpdateDto getBookImage(Long id) {
        Book book = findBook(id);
        return new BookImageUpdateDto(book);
    }

    public Book findBook(Long id) {
        return bookRepository.findByIdAndDeletedFalse(id)
                .orElseThrow(() -> new EntityNotFoundException("책을 찾을 수 없습니다."));
    }

    private PageRequest getPageable(int pageNo, int size, String sortType) {
        String[] sortParts = sortType.split("_");
        String sortField = sortParts[0];
        String direction = sortParts[1];
        Sort sort = Sort.by(Sort.Direction.fromString(direction), sortField);
        return PageRequest.of(pageNo, size, sort);
    }

}
