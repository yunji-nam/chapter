package com.example.chapter.dto;

import com.example.chapter.entity.DeliveryStatus;
import com.example.chapter.entity.Order;
import com.example.chapter.entity.OrderStatus;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderListDto {
    private Long id;
    private String merchantUid;
    private List<OrderBookInfo> books;
    private String username;
    private OrderStatus orderStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;
    private DeliveryStatus deliveryStatus;
    private int totalPrice;

    public OrderListDto(Order order) {
        this.id = order.getId();
        this.merchantUid = order.getMerchantUid();
        this.books = order.getItems().stream()
                .map(OrderBookInfo::new)
                .collect(Collectors.toList());
        this.username = order.getUser().getName();
        this.orderStatus = order.getStatus();
        this.orderDate = order.getCreatedAt();
        this.deliveryStatus = order.getDelivery().getStatus();
        this.totalPrice = books.stream()
                .mapToInt(book -> book.getPrice() * book.getQuantity())
                .sum();
    }

}
