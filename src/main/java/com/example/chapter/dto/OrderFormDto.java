package com.example.chapter.dto;

import com.example.chapter.entity.Address;
import com.example.chapter.entity.Order;
import com.example.chapter.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderFormDto {
    private String merchantUid;
    private String userName;
    private String userEmail;
    private String userPhone;
    private Address userAddress;
    private List<OrderItemDto> orderItems;
    private int totalPrice;

    public OrderFormDto(Order order, User user, List<OrderItemDto> orderItems) {
        this.merchantUid = order.getMerchantUid();
        this.userName = user.getName();
        this.userEmail = user.getEmail();
        this.userPhone = user.getPhone();
        this.userAddress = user.getAddress();
        this.orderItems = orderItems;
        this.totalPrice = orderItems.stream().mapToInt(OrderItemDto::getBookPrice).sum();
    }
}