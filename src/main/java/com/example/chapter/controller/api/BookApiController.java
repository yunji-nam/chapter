package com.example.chapter.controller.api;

import com.example.chapter.dto.ApiResponse;
import com.example.chapter.dto.BookStatusUpdateDto;
import com.example.chapter.service.BookService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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

    // 도서 이미지 수정
    @PutMapping("/admin/book/{bookId}/cover")
    public ApiResponse<Object> updateBookImage(@PathVariable Long bookId, @RequestParam(value = "image") MultipartFile file) {
        String imageUrl = bookService.updateBookImage(bookId, file);
        return ApiResponse.builder().data(imageUrl).message("도서 이미지 변경 완료").build();
    }
}
