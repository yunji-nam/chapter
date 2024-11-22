package com.example.chapter.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class CartItemDto {

    private Long bookId;
    private String bookTitle;
    private int bookPrice;
    private String bookImage;
    @Min(1)
    private int quantity;

    public CartItemDto(Long id, String title, int price, String imageUrl, int quantity) {
        this.bookId = id;
        this.bookTitle = title;
        this.bookPrice = price;
        this.bookImage = imageUrl;
        this.quantity = quantity;
    }

}
