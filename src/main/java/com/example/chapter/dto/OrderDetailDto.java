package com.example.chapter.dto;

import com.example.chapter.entity.*;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderDetailDto {

    private Long id;
    private String merchantUid;
    private List<OrderBookInfo> books;
    private int totalPrice;
    private OrderStatus orderStatus;
    private LocalDateTime orderDate;
    private DeliveryStatus deliveryStatus;
    private String username;
    private String phone;
    private String streetAddress;
    private String detailAddress;

    public OrderDetailDto(Order order, List<OrderBookInfo> books) {
        this.id = order.getId();
        this.merchantUid = order.getMerchantUid();
        this.books = books;
        this.totalPrice = books.stream()
            .mapToInt(book -> book.getPrice() * book.getQuantity())
            .sum();
        this.orderStatus = order.getStatus();
        this.orderDate = order.getCreatedAt();
        this.deliveryStatus = order.getDelivery().getStatus();
        this.username = order.getUser().getName();
        this.phone = order.getUser().getPhone();
        this.streetAddress = order.getDelivery().getAddress().getStreet();
        this.detailAddress = order.getDelivery().getAddress().getDetail();
    }

    public List<Integer> getQuantities() {
        return books.stream().map(OrderBookInfo::getQuantity).collect(Collectors.toList());
    }

}

