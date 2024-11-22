package com.example.chapter.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class CartUpdateDto {

    Long bookId;
    @Min(1)
    int quantity;
}
