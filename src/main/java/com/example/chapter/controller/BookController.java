package com.example.chapter.controller;

import com.example.chapter.dto.BookRegistrationDto;
import com.example.chapter.dto.BookResponseDto;
import com.example.chapter.entity.Category;
import com.example.chapter.security.UserDetailsImpl;
import com.example.chapter.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
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
    public String registerBookForm(Model model) {
        model.addAttribute("form", new BookRegistrationDto());
        model.addAttribute("categories", Category.values());
        return "admin/book/add";
    }

    /**
     * 도서 등록
     * @param registrationDto
     * @param image
     * @return
     */
    @PostMapping("/book")
    public String registerBook(@Valid @RequestPart(value = "data") BookRegistrationDto registrationDto,
                          @RequestPart(value = "image", required = false) MultipartFile image,
                          @AuthenticationPrincipal UserDetailsImpl userDetails) {
        bookService.registerBook(registrationDto, image, userDetails.getUser());
        return "redirect:/books";
    }


    @GetMapping("/books")
    public String getAllBooks(@RequestParam(required = false) String category,
                              @RequestParam(defaultValue = "id") String sortType,
                              @RequestParam(defaultValue = "0") int pageNo,
                              @RequestParam(defaultValue = "2") int size,
                              Model model) {
        Page<BookResponseDto> bookDtos;

        if (category != null) {
            bookDtos = bookService.getBooksByCategory(Category.valueOf(category), sortType, pageNo, size);
        } else {
            bookDtos = bookService.getBooks(sortType, pageNo, size);
        }
        model.addAttribute("bookList", bookDtos);
        return "book/list";
    }

    @GetMapping("/book/{bookId}")
    public String getBook(@PathVariable Long bookId, Model model) {
        BookResponseDto dto = bookService.getBook(bookId);
        model.addAttribute("book", dto);

        return "book/detail";
    }

    //도서 수정 폼
    @GetMapping("/book/{bookId}/edit")
    public String updateBookForm(@PathVariable Long bookId, Model model) {
        BookResponseDto responseDto = bookService.getBook(bookId);
        model.addAttribute("form", new BookRegistrationDto(responseDto));
        model.addAttribute("categories", Category.values());
        return "admin/book/update";
    }

    @PutMapping("/book/{bookId}/edit")
    public String updateBook(@PathVariable Long bookId,
                             @Valid @RequestPart(value = "data") BookRegistrationDto registrationDto,
                             @RequestPart(value = "image", required = false) MultipartFile image,
                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        bookService.updateBook(bookId, registrationDto, image, userDetails.getUser());
        return "redirect:/book/" + bookId;
    }

    @DeleteMapping("/book/{bookId}")
    public String deleteBook(@PathVariable Long bookId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        bookService.deleteBook(bookId, userDetails.getUser());
        return "redirect:/books";
    }
}