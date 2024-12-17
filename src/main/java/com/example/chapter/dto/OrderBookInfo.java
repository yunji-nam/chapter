package com.example.chapter.dto;

import com.example.chapter.entity.OrderItem;
import com.example.chapter.entity.ReviewStatus;
import lombok.Getter;

@Getter
public class OrderBookInfo {
    private Long orderItemId;
    private Long bookId;
    private String title;
    private String author;
    private String image;
    private int price;
    private int quantity;
    private ReviewStatus reviewStatus;

    public OrderBookInfo(OrderItem orderItem, ReviewStatus status) {
        this.orderItemId = orderItem.getId();
        this.bookId = orderItem.getBook().getId();
        this.title = orderItem.getBook().getTitle();
        this.author = orderItem.getBook().getAuthor();
        this.image = orderItem.getBook().getImage();
        this.price = orderItem.getBook().getPrice();
        this.quantity = orderItem.getQuantity();
        this.reviewStatus = status;
    }

    public OrderBookInfo(OrderItem orderItem) {
        this.bookId = orderItem.getBook().getId();
        this.title = orderItem.getBook().getTitle();
        this.author = orderItem.getBook().getAuthor();
        this.image = orderItem.getBook().getImage();
        this.price = orderItem.getBook().getPrice();
        this.quantity = orderItem.getQuantity();
    }
}
