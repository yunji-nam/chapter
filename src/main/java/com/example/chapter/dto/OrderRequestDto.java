package com.example.chapter.dto;

import com.example.chapter.entity.Address;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
public class OrderRequestDto {

    @NotEmpty
    private List<OrderItemDto> orderItems;
    @NotEmpty
    private String deliveryName;
    @NotEmpty
    private String deliveryPhone;
    private Address deliveryAddress;
    @NotNull
    private BigDecimal amount;
    @NotEmpty
    private String payMethod;

}
