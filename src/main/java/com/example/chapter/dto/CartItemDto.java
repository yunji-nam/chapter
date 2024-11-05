package com.example.chapter.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class CartItemDto {

    private String title;
    private int price;
//    private String bookImage;
    @Min(1)
    private int quantity;

    public CartItemDto(String title, int price, int quantity) {
        this.title = title;
        this.price = price;
        this.quantity = quantity;
    }

}
