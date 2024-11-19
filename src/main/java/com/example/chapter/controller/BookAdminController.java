package com.example.chapter.controller;

import com.example.chapter.dto.BookRegistrationDto;
import com.example.chapter.dto.BookResponseDto;
import com.example.chapter.entity.Category;
import com.example.chapter.security.UserDetailsImpl;
import com.example.chapter.service.BookService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequiredArgsConstructor
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
    public String registerBook(@Valid BookRegistrationDto form,
                               @AuthenticationPrincipal UserDetailsImpl userDetails) {
        bookService.registerBook(form, userDetails.getUser());
        return "redirect:/books";
    }

    //도서 수정 폼
    @GetMapping("/book/{bookId}/edit")
    public String updateBookForm(@PathVariable Long bookId, Model model) {
        BookResponseDto responseDto = bookService.getBook(bookId);
        model.addAttribute("form", new BookRegistrationDto(responseDto));
        model.addAttribute("categories", Category.values());
        return "admin/book/update";
    }

    // 도서 수정
    @PutMapping("/book/{bookId}/edit")
    public String updateBook(@PathVariable Long bookId,
                             @Valid BookRegistrationDto form,
                             @AuthenticationPrincipal UserDetailsImpl userDetails) {
        bookService.updateBook(bookId, form, userDetails.getUser());
        return "redirect:/book/" + bookId;
    }

    // 도서 삭제
    @DeleteMapping("/book/{bookId}")
    public String deleteBook(@PathVariable Long bookId, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        bookService.deleteBook(bookId, userDetails.getUser());
        return "redirect:/books";
    }
}
