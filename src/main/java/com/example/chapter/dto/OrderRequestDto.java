package com.example.chapter.dto;

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
    @NotEmpty(message = "수령인을 입력해주세요.")
    private String deliveryName;
    @NotEmpty(message = "연락처를 입력해주세요.")
    private String deliveryPhone;
    @NotEmpty(message = "우편번호를 입력해주세요.")
    private String deliveryZipcode;
    @NotEmpty(message = "도로명 주소를 입력해주세요.")
    private String deliveryStreet;
    private String deliveryDetail;
    @NotNull
    private BigDecimal amount;
    @NotEmpty
    private String payMethod;

}
