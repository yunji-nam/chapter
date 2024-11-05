package com.example.chapter.dto;

import com.example.chapter.entity.*;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderDetailDto {

    private Long id;
    private List<BookInfo> books;
    private int totalPrice;
    private OrderStatus orderStatus;
    private LocalDateTime orderDate;
    private DeliveryStatus deliveryStatus;
    private String userName;
    private String phone;
    private Address address;

    public OrderDetailDto(Order order) {
        this.id = order.getId();
        this.books = order.getItems().stream()
            .map(BookInfo::new)
            .collect(Collectors.toList());
        this.totalPrice = books.stream()
            .mapToInt(book -> book.getPrice() * book.getQuantity())
            .sum();
        this.orderStatus = order.getStatus();
        this.orderDate = order.getOrderDate();
        this.deliveryStatus = order.getDelivery().getStatus();
        this.userName = order.getUser().getName();
        this.phone = order.getUser().getPhone();
        this.address = order.getDelivery().getAddress();
    }

}

