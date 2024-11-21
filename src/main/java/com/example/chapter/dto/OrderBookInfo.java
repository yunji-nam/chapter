package com.example.chapter.dto;

import com.example.chapter.entity.OrderItem;
import lombok.Getter;

@Getter
public class OrderBookInfo {
    private String title;
    private String author;
    private int price;
    private int quantity;

    public OrderBookInfo(OrderItem orderItem) {
        this.title = orderItem.getBook().getTitle();
        this.author = orderItem.getBook().getAuthor();
        this.price = orderItem.getBook().getPrice();
        this.quantity = orderItem.getQuantity();
    }
}
