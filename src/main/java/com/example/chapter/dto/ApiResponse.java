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

    private String msg;
    private T data;

    public ApiResponse(String msg) {
        this.msg = msg;
        this.data = null;
    }

}