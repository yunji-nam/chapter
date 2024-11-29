package com.example.chapter.controller;

import com.example.chapter.dto.BookDetailDto;
import com.example.chapter.dto.BookListDto;
import com.example.chapter.entity.Category;
import com.example.chapter.service.BookService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@Slf4j
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    // 도서 모두 조회
    @GetMapping("/books")
    public String getAllBooks(@RequestParam(required = false) String category,
                              @RequestParam(defaultValue = "id") String sortType,
                              @RequestParam(defaultValue = "0") int pageNo,
                              @RequestParam(defaultValue = "2") int size,
                              Model model) {
        Page<BookListDto> bookDtos;

        if (category == null || category.isEmpty()) {
            bookDtos = bookService.getBooks(sortType, pageNo, size);
        } else {
            try {
                bookDtos = bookService.getBooksByCategory(Category.valueOf(category), sortType, pageNo, size);
            } catch (IllegalArgumentException e) {
                bookDtos = bookService.getBooks(sortType, pageNo, size);
            }
        }

        model.addAttribute("bookList", bookDtos);
        return "book/list";
    }

    // 도서 상세 조회
    @GetMapping("/book/{bookId}")
    public String getBook(@PathVariable Long bookId, Model model) {
        BookDetailDto dto = bookService.getBook(bookId);
        model.addAttribute("book", dto);

        return "book/detail";
    }

    @GetMapping("/search")
    public String searchBook(@RequestParam String keyword,
                             @PageableDefault(size = 3) Pageable pageable,
                             Model model) {
        Page<BookListDto> searchResults = bookService.searchBook(keyword, pageable);
        model.addAttribute("searchResults", searchResults);
        model.addAttribute("keyword", keyword);

        return "book/search";
    }

}