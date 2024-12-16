package com.example.chapter.dto;

import com.example.chapter.entity.OrderItem;
import lombok.Getter;

@Getter
public class OrderBookInfo {
    private Long bookId;
    private String title;
    private String author;
    private String image;
    private int price;
    private int quantity;
    private boolean reviewable;

    public OrderBookInfo(OrderItem orderItem, boolean reviewable) {
        this.bookId = orderItem.getBook().getId();
        this.title = orderItem.getBook().getTitle();
        this.author = orderItem.getBook().getAuthor();
        this.image = orderItem.getBook().getImage();
        this.price = orderItem.getBook().getPrice();
        this.quantity = orderItem.getQuantity();
        this.reviewable = reviewable;
    }
}
