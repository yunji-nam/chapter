package com.example.chapter.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {

    private Long bookId;
    private int quantity;
}
