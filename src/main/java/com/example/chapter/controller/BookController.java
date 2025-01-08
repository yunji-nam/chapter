package com.example.chapter.controller;

import com.example.chapter.dto.BookDetailDto;
import com.example.chapter.dto.BookListDto;
import com.example.chapter.entity.Category;
import com.example.chapter.security.UserDetailsImpl;
import com.example.chapter.service.BookService;
import com.example.chapter.service.LikeService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
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
    private final LikeService likeService;

    // 도서 모두 조회
    @GetMapping("/books")
    public String getAllBooks(@RequestParam(required = false) Category category,
                              @RequestParam(defaultValue = "id") String sortType,
                              @RequestParam(defaultValue = "0") int pageNo,
                              @RequestParam(defaultValue = "3") int size,
                              @AuthenticationPrincipal UserDetailsImpl userDetails,
                              Model model) {
        Page<BookListDto> bookDtos;

        if (category == null) {
            bookDtos = bookService.getBooks(sortType, pageNo, size);
        } else {
            try {
                bookDtos = bookService.getBooksByCategory(category, sortType, pageNo, size);
            } catch (IllegalArgumentException e) {
                bookDtos = bookService.getBooks(sortType, pageNo, size);
            }
        }

        if (userDetails != null) {
            for (BookListDto book : bookDtos.getContent()) {
                book.setLikeStatus(likeService.getLikeStatus(userDetails.getUser(), book.getId()));
                log.info("book Id:{}", book.getId());
                log.info("likeStatus:{}", book.isLikeStatus());
            }
        }

        model.addAttribute("categories", Category.values());
        model.addAttribute("requestedCategory", category);
        model.addAttribute("bookList", bookDtos);

        if (bookDtos.getContent().isEmpty()) {
            return "book/empty";
        }
        return "book/list";
    }

    // 도서 상세 조회
    @GetMapping("/book/{bookId}")
    public String getBook(@PathVariable Long bookId, Model model, @AuthenticationPrincipal UserDetailsImpl userDetails) {
        BookDetailDto dto = bookService.getBook(bookId);
        model.addAttribute("book", dto);

        boolean likeStatus = false;
        if (userDetails != null) {
            likeStatus = likeService.getLikeStatus(userDetails.getUser(), bookId);
        }
        model.addAttribute("likeStatus", likeStatus);
        log.info("like status{}", likeStatus);

        return "book/detail";
    }

    // 도서 검색
    @GetMapping("/search")
    public String searchBook(@RequestParam String keyword,
                             @PageableDefault(size = 3) Pageable pageable,
                             Model model) {
        Page<BookListDto> searchResults = bookService.searchBook(keyword, pageable);
        model.addAttribute("bookList", searchResults);
        model.addAttribute("keyword", keyword);

        if (searchResults.getContent().isEmpty()) {
            return "search/empty";
        }
        return "search/result";
    }

    @GetMapping("/books/best")
    public String getBooksByBestSelling(@PageableDefault(size = 3) Pageable pageable, Model model) {
        Page<BookListDto> booksByBestSelling = bookService.getBooksByBestSelling(pageable);
        model.addAttribute("bookList", booksByBestSelling);
        return "book/best";
    }

    @GetMapping("/books/new")
    public String getBooksByLatest(@PageableDefault(size = 3) Pageable pageable, Model model) {
        Page<BookListDto> booksByLatest = bookService.getBooksByLatest(pageable);
        model.addAttribute("bookList", booksByLatest);
        return "book/new";
    }
}