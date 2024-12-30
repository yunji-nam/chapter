package com.example.chapter.entity;

import lombok.Getter;

@Getter
public enum ReviewStatus {
    CAN_WRITE("작성가능"), COMPLETED("작성완료"), NOT_ALLOWED("작성불가");

    private final String statusName;

    ReviewStatus(String statusName) {
        this.statusName = statusName;
    }
}