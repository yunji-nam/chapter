package com.example.chapter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDate;

@Getter
@AllArgsConstructor
public class ReviewResponseDto {

    private Long id;
    private String userName;
    private String content;
    private int rating;
    private LocalDate createdDate;
    private LocalDate modifiedDate;

}
