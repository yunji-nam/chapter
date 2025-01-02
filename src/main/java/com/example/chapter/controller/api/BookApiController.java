package com.example.chapter.controller.api;

import com.example.chapter.dto.ApiResponse;
import com.example.chapter.dto.BookStatusUpdateDto;
import com.example.chapter.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class BookApiController {

    private final BookService bookService;

    @PutMapping("/admin/book/status")
    public ApiResponse<String> updateBookStatus(@RequestBody BookStatusUpdateDto dto) {
        bookService.updateBookStatus(dto);
        return new ApiResponse<>("도서 상태 변경 완료");
    }
}
