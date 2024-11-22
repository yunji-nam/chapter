package com.example.chapter.dto;

import com.example.chapter.entity.Address;
import com.example.chapter.entity.User;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@NoArgsConstructor
public class OrderRequestDto {

    @NotEmpty
    private String deliveryName;
    @NotEmpty
    private String deliveryPhone;
    private Address deliveryAddress;

    private String userName;
    private String userPhone;
    private Address userAddress;
    private List<CartItemDto> cartItems;
    private int totalPrice;

    public OrderRequestDto(User user, List<CartItemDto> cartItems) {
        this.userName = user.getName();
        this.userAddress = user.getAddress();
        this.userPhone = user.getPhone();
        this.cartItems = cartItems;
        this.totalPrice = cartItems.stream().mapToInt(CartItemDto::getBookPrice).sum();
    }
}
