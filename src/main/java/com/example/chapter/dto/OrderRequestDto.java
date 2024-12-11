package com.example.chapter.dto;

import com.example.chapter.entity.Address;
import com.example.chapter.entity.OrderItem;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderRequestDto {

    private String merchantUid; // 주문번호
    private List<Long> cartItemIds;

    @NotEmpty
    private String deliveryName;
    @NotEmpty
    private String deliveryPhone;
    private Address deliveryAddress;

}
