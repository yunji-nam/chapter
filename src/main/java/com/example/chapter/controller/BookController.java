package com.example.chapter.controller;

import com.example.chapter.dto.BookRegistrationDto;
import com.example.chapter.dto.BookResponseDto;
import com.example.chapter.entity.Category;
import com.example.chapter.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    /**
     * 도서 등록 폼
     * @param model
     * @return
     */
    @GetMapping("/book")
    public String addBookForm(Model model) {
        model.addAttribute("form", new BookRegistrationDto());
        model.addAttribute("categories", Category.values());
        return "book/admin/bookForm";
    }

    /**
     * 도서 등록
     * @param registrationDto
     * @param image
     * @return
     */
    @PostMapping("/book")
    public String addBook(@Valid @RequestPart(value = "data") BookRegistrationDto registrationDto,
                                          @RequestPart(value = "image") MultipartFile image) {
        bookService.addBook(registrationDto, image);
        return "/book/list";
    }

    @GetMapping("/books")
    public String getAllBooks(@RequestParam(required = false) String category,
                              @RequestParam(defaultValue = "latest") String sortType,
                              @RequestParam(defaultValue = "0") int pageNo,
                              @RequestParam(defaultValue = "2") int size,
                              Model model) {
        Page<BookResponseDto> bookDtos;

        if (category != null) {
            bookDtos = bookService.getBooksByCategory(Category.valueOf(category), sortType, pageNo, size);
        } else {
            bookDtos = bookService.getAllBooks(sortType, pageNo, size);
        }
        model.addAttribute("bookList", bookDtos);
        return "book/list";
    }



}