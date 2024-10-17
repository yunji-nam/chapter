package com.example.chapter.dto;

import lombok.Getter;

@Getter
public class CartItemDto {

    private String bookTitle;
    private int bookPrice;
//    private String bookImage;
    private int quantity;

    public CartItemDto(String bookTitle, int bookPrice, int quantity) {
        this.bookTitle = bookTitle;
        this.bookPrice = bookPrice;
        this.quantity = quantity;
    }

}
