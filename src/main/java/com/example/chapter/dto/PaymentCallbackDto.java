package com.example.chapter.dto;

import lombok.Getter;

@Getter
public class PaymentCallbackDto {

    private String impUid; // 결제 고유 번호
    private int requestPrice; // 요청 결제 금액

}
