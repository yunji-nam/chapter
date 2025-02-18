package com.example.chapter.dto;

import com.example.chapter.entity.Category;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class BookSalesDto {

    private Long bookId;
    private Category category;
    private String bookTitle;
    private String bookAuthor;
    private int bookPrice;
    private Long totalQuantity;
    private Long totalSales;
}
