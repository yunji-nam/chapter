package com.example.chapter.repository;

import com.example.chapter.dto.BookListDto;
import com.example.chapter.entity.Book;
import com.example.chapter.entity.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface BookRepository extends JpaRepository<Book, Long> {
    Page<Book> findByCategory(Category category, Pageable pageable);

    @Query("select b from Book b where b.title like %:kw% " +
            "or b.author like %:kw% " +
            "or b.publisher like %:kw%")
    Page<BookListDto> findAllByKeywordContainsIgnoreCase(@Param("kw") String kw, Pageable pageable);

}
