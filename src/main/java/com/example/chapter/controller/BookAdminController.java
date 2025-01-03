package com.example.chapter.controller;

import com.example.chapter.dto.BookDetailDto;
import com.example.chapter.dto.BookListDto;
import com.example.chapter.dto.BookRegistrationDto;
import com.example.chapter.entity.Category;
import com.example.chapter.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
@Slf4j
@RequestMapping("/admin")
public class BookAdminController {

    private final BookService bookService;

    // 도서 등록 폼
    @GetMapping("/book")
    public String registerBookForm(Model model) {
        model.addAttribute("form", new BookRegistrationDto());
        model.addAttribute("categories", Category.values());
        return "admin/book/add";
    }

    // 도서 등록
    @PostMapping("/book")
    public String registerBook(@Valid BookRegistrationDto form) {
        bookService.registerBook(form);
        return "redirect:/admin/books";
    }

    //도서 수정 폼
    @GetMapping("/book/{bookId}/edit")
    public String updateBookForm(@PathVariable Long bookId, Model model) {
        BookDetailDto responseDto = bookService.getBook(bookId);
        BookRegistrationDto form = new BookRegistrationDto(responseDto);
        model.addAttribute("form", new BookRegistrationDto(responseDto));
        model.addAttribute("categories", Category.values());
        return "admin/book/update";
    }

    // 도서 정보 수정
    @PutMapping("/book/{bookId}/edit")
    public String updateBook(@PathVariable Long bookId,
                             @Valid BookRegistrationDto form) {
        bookService.updateBook(bookId, form);
        return "redirect:/admin/books";
    }

    // 도서 삭제
    @DeleteMapping("/book/{bookId}")
    public String deleteBook(@PathVariable Long bookId) {
        bookService.deleteBook(bookId);
        return "redirect:/admin/books";
    }


    // 도서 목록 조회
    @GetMapping("/books")
    public String getAllBooks(@RequestParam(required = false) String category,
                              @RequestParam(defaultValue = "id") String sortType,
                              @RequestParam(defaultValue = "0") int pageNo,
                              @RequestParam(defaultValue = "5") int size,
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
        return "admin/book/list";
    }
}
