package com.example.chapter.dto;

import jakarta.validation.constraints.Min;
import lombok.Getter;

@Getter
public class OrderRequestDto {

    private Long bookId;
    @Min(1)
    private int count;
}
