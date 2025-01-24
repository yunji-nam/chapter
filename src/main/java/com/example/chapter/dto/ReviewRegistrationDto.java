package com.example.chapter.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ReviewRegistrationDto {

    @NotEmpty
    @Size(min = 10, message = "최소 10자 이상이어야 합니다.")
    private String content;
    private int rating;

    public ReviewRegistrationDto(ReviewResponseDto dto) {
        this.content = dto.getContent();
        this.rating = dto.getRating();
    }
}
