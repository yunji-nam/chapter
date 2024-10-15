package com.example.chapter.dto;

import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewRegistrationDto {

    private String content;
    private int rating;

    public ReviewRegistrationDto(ReviewResponseDto dto) {
        this.content = dto.getContent();
        this.rating = dto.getRating();
    }
}
