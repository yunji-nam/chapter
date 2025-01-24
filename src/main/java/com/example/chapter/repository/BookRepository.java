package com.example.chapter.repository;

import com.example.chapter.dto.BookListDto;
import com.example.chapter.entity.Book;
import com.example.chapter.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.Optional;

public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findAllByDeletedFalse(Pageable pageable);

    Page<Book> findByCategoryAndDeletedFalse(Category category, Pageable pageable);

    @Query(value = "select b from Book b where b.title like %:kw% " +
            "or b.author like %:kw% " +
            "or b.publisher like %:kw%")
    Page<BookListDto> findAllByKeywordIgnoreCaseAndDeletedFalse(String kw, Pageable pageable);

    @Query(value = "select b from Book b " +
            "left join OrderItem oi on b.id = oi.book.id " +
            "left join Order o on oi.order.id = o.id " +
            "where o.createdAt >= :startedAt and b.deleted = false group by b.id " +
            "order by sum(oi.quantity) desc")
    Page<BookListDto> findBestSellingBooks(LocalDateTime startedAt, Pageable pageable);

    Page<Book> findByCreatedAtBetweenAndDeletedFalseOrderByIdDesc(LocalDateTime startedAt, LocalDateTime endedAt, Pageable pageable);

    Page<Book> findByTitleContainingIgnoreCaseAndDeletedFalse(String title, Pageable pageable);
    Page<Book> findByAuthorContainingIgnoreCaseAndDeletedFalse(String author, Pageable pageable);
    Page<Book> findByPublisherContainingIgnoreCaseAndDeletedFalse(String publisher, Pageable pageable);
    Page<Book> findByIsbnContainingIgnoreCaseAndDeletedFalse(String isbn, Pageable pageable);

    Optional<Book> findByIdAndDeletedFalse(Long id);
}
