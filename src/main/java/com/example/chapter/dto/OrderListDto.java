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
    private List<OrderBookInfo> books;
    private OrderStatus orderStatus;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime orderDate;
    private DeliveryStatus deliveryStatus;

    public OrderListDto(Order order) {
        this.id = order.getId();
        this.books = order.getItems().stream()
                .map(OrderBookInfo::new)
                .collect(Collectors.toList());
        this.orderStatus = order.getStatus();
        this.orderDate = order.getOrderDate();
        this.deliveryStatus = order.getDelivery().getStatus();
    }

}
