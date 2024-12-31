package com.example.chapter.entity;

import lombok.Getter;

@Getter
public enum BookStatus {
    SELL("판매중"), OUT_OF_STOCK("품절");

    private final String statusName;

    BookStatus(String statusName) {
        this.statusName = statusName;
    }
}