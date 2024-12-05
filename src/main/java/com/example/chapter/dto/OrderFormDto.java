package com.example.chapter.dto;

import com.example.chapter.entity.Address;
import com.example.chapter.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderFormDto {
    private String userName;
    private String userPhone;
    private Address userAddress;
    private List<OrderItemDto> orderItems;
    private int totalPrice;

    public OrderFormDto(User user, List<OrderItemDto> orderItems) {
        this.userName = user.getName();
        this.userPhone = user.getPhone();
        this.userAddress = user.getAddress();
        this.orderItems = orderItems;
        this.totalPrice = orderItems.stream().mapToInt(OrderItemDto::getBookPrice).sum();
    }
}