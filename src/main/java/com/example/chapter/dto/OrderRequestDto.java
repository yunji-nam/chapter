package com.example.chapter.dto;

import com.example.chapter.entity.Address;
import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class OrderRequestDto {

    private Long orderId;

    @NotEmpty
    private String deliveryName;
    @NotEmpty
    private String deliveryPhone;
    private Address deliveryAddress;

}
