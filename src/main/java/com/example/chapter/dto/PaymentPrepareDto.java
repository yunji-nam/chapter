package com.example.chapter.dto;

import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Setter
public class PaymentPrepareDto {

    private String merchant_uid;
    private BigDecimal amount;

}
