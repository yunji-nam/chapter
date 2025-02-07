package com.example.chapter.dto;

import com.example.chapter.entity.Order;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Getter
public class OrderSummaryDto {
    private Long id;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;
    private String merchantUid;
    private List<OrderBookInfo> books;
    private int totalPrice;

    public OrderSummaryDto(Order order) {
        this.id = order.getId();
        this.orderDate = order.getCreatedAt();
        this.merchantUid = order.getMerchantUid();
        this.books = order.getItems().stream()
                        .map(OrderBookInfo::new)
                        .collect(Collectors.toList());
        this.totalPrice = books.stream()
                        .mapToInt(book -> book.getPrice() * book.getQuantity())
                        .sum();
    }
}
