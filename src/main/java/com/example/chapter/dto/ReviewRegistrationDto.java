package com.example.chapter.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor
public class ReviewRegistrationDto {

    @NotEmpty
    private String content;
    private int rating;

    public ReviewRegistrationDto(ReviewResponseDto dto) {
        this.content = dto.getContent();
        this.rating = dto.getRating();
    }
}
