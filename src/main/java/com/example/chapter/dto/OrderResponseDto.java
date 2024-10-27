package com.example.chapter.dto;

import com.example.chapter.entity.*;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderResponseDto {

    private Long id;
    private List<BookInfo> books;
    private int totalPrice;
    private OrderStatus orderStatus;
    private DeliveryStatus deliveryStatus;
    private String userName;
    private String phone;
    private Address address;

    public OrderResponseDto(Order order) {
        this.id = order.getId();
        this.books = order.getOrderBooks().stream()
            .map(BookInfo::new)
            .collect(Collectors.toList());
        this.totalPrice = books.stream()
            .mapToInt(book -> book.getPrice() * book.getQuantity())
            .sum();
        this.orderStatus = order.getStatus();
        this.deliveryStatus = order.getDelivery().getStatus();
        this.userName = order.getUser().getName();
        this.phone = order.getUser().getPhone();
        this.address = order.getDelivery().getAddress();
    }

    @Getter
    public static class BookInfo {
        private String title;
        private String author;
        private int price;
        private int quantity;

        public BookInfo(OrderBook orderBook) {
            this.title = orderBook.getBook().getTitle();
            this.author = orderBook.getBook().getAuthor();
            this.price = orderBook.getBook().getPrice();
            this.quantity = orderBook.getQuantity();
        }
    }
}

