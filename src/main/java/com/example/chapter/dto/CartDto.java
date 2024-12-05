package com.example.chapter.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class CartDto {

    private Long cartItemId;
    private Long bookId;
    @Min(1)
    private int quantity;
}
