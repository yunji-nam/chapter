package com.example.chapter.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class CartDto {

    private Long cartItemId;
    private Long bookId;
    @Min(1)
    private int quantity;
}
