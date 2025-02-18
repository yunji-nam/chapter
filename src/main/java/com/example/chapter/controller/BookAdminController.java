package com.example.chapter.controller;

import com.example.chapter.dto.BookDetailDto;
import com.example.chapter.dto.BookImageUpdateDto;
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
import org.springframework.validation.BindingResult;
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
        model.addAttribute("bookRegistrationDto", new BookRegistrationDto());
        model.addAttribute("categories", Category.values());
        return "admin/book/add";
    }

    // 도서 등록
    @PostMapping("/book")
    public String registerBook(@Valid BookRegistrationDto bookRegistrationDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", Category.values());
            return "admin/book/add";
        }
        bookService.registerBook(bookRegistrationDto);
        return "redirect:/admin/books";
    }

    //도서 수정 폼
    @GetMapping("/book/{bookId}")
    public String updateBookForm(@PathVariable Long bookId, Model model) {
        BookDetailDto responseDto = bookService.getBook(bookId);
        model.addAttribute("bookRegistrationDto", new BookRegistrationDto(responseDto));
        model.addAttribute("categories", Category.values());
        return "admin/book/updateInfo";
    }

    // 도서 정보 수정
    @PutMapping("/book/{bookId}")
    public String updateBook(@PathVariable Long bookId, @Valid BookRegistrationDto bookRegistrationDto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("categories", Category.values());
            return "admin/book/updateInfo";
        }
        bookService.updateBook(bookId, bookRegistrationDto);
        return "redirect:/admin/books";
    }

    // 도서 이미지 수정 폼
    @GetMapping("/book/{bookId}/image")
    public String updateBookImageForm(@PathVariable Long bookId, Model model) {
        BookImageUpdateDto dto = bookService.getBookImage(bookId);
        model.addAttribute("book", dto);
        return "admin/book/updateImage";
    }

    // 도서 목록 조회
    @GetMapping("/books")
    public String getAllBooks(@RequestParam(required = false) String category,
                              @RequestParam(defaultValue = "id_desc") String sortType,
                              @RequestParam(defaultValue = "0") int pageNo,
                              @RequestParam(defaultValue = "10") int size,
                              Model model) {
        Page<BookListDto> bookDtos;

        if (category == null || category.isEmpty()) {
            bookDtos = bookService.getBooksForAdmin(sortType, pageNo, size);
        } else {
            try {
                bookDtos = bookService.getBooksByCategoryForAdmin(Category.valueOf(category), sortType, pageNo, size);
            } catch (IllegalArgumentException e) {
                bookDtos = bookService.getBooksForAdmin(sortType, pageNo, size);
            }
        }

        model.addAttribute("bookList", bookDtos);
        model.addAttribute("categories", Category.values());
        model.addAttribute("requestedCategory", category);
        model.addAttribute("sortType", sortType);

        return "admin/book/list";
    }

    @GetMapping("/book/search")
    public String searchBook(@RequestParam(required = false) String category,
                             @RequestParam(defaultValue = "id_desc") String sortType,
                             @RequestParam String query,
                             @RequestParam String condition,
                             @RequestParam(defaultValue = "0") int pageNo,
                             @RequestParam(defaultValue = "10") int size,
                             Model model) {
        Category categoryValue = null;
        if (category != null && !category.isEmpty()) {
            try {
                categoryValue = Category.valueOf(category);
            } catch (IllegalArgumentException e) {
                categoryValue = null;
            }
        }
        Page<BookListDto> searchResults = bookService.searchBookByCondition(categoryValue, sortType, query, condition, pageNo, size);

        model.addAttribute("bookList", searchResults);
        model.addAttribute("sortType", sortType);
        model.addAttribute("query", query);
        model.addAttribute("condition", condition);
        model.addAttribute("categories", Category.values());
        model.addAttribute("requestedCategory", category);

        return "admin/book/search";
    }
}
