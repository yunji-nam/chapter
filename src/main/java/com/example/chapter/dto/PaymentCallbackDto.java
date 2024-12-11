package com.example.chapter.dto;

import lombok.Getter;

@Getter
public class PaymentCallbackDto {

    private String merchantUid; // 주문 고유 번호
    private String impUid; // 결제 고유 번호

}
