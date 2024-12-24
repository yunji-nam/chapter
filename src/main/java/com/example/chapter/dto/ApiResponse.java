package com.example.chapter.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@Builder
public class ApiResponse<T> {

    private String message;
    private T data;

    public ApiResponse(String message) {
        this.message = message;
        this.data = null;
    }

}