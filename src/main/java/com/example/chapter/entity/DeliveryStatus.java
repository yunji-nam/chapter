package com.example.chapter.entity;

import lombok.Getter;

@Getter
public enum DeliveryStatus {
    READY("배송준비중"), SHIPPING("배송중"), DELIVERED("배송완료");

    private final String statusName;

    DeliveryStatus(String statusName) {
        this.statusName = statusName;
    }
}
