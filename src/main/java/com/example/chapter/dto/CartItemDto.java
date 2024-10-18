package com.example.chapter.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class CartItemDto {

    private String bookTitle;
    private int bookPrice;
//    private String bookImage;
    @Min(1)
    private int quantity;

    public CartItemDto(String bookTitle, int bookPrice, int quantity) {
        this.bookTitle = bookTitle;
        this.bookPrice = bookPrice;
        this.quantity = quantity;
    }

}
