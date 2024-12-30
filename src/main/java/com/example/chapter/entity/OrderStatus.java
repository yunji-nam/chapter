package com.example.chapter.entity;

import lombok.Getter;

@Getter
public enum OrderStatus {
    PAID("결제완료"), CANCEL("취소");

    private final String statusName;

    OrderStatus(String statusName) {
        this.statusName = statusName;
    }
}