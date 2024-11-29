package com.example.chapter.dto;

import com.example.chapter.entity.CartItem;
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

    public CartItemDto(CartItem cartItem) {
        this.bookId = cartItem.getBook().getId();
        this.bookTitle = cartItem.getBook().getTitle();
        this.bookPrice = cartItem.getBook().getPrice();
        this.bookImage = cartItem.getBook().getImage();
        this.quantity = cartItem.getQuantity();
    }

}
