package com.example.chapter.dto;

import com.example.chapter.entity.Address;
import com.example.chapter.entity.Order;
import com.example.chapter.entity.User;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderFormDto {
    private String merchantUid;
    private String username;
    private String userEmail;
    private String userPhone;
    private Address userAddress;
    private Long orderId;

    public OrderFormDto(String merchantUid, User user) {
        this.merchantUid = merchantUid;
        this.username = user.getName();
        this.userEmail = user.getEmail();
        this.userPhone = user.getPhone();
        this.userAddress = user.getAddress();
    }

    public OrderFormDto(Order order) {
        this.username = order.getDelivery().getName();
        this.userPhone = order.getDelivery().getPhone();
        this.userAddress = order.getDelivery().getAddress();
        this.orderId = order.getId();
    }
}