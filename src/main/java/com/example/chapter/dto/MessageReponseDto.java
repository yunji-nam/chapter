package com.example.chapter.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public class MessageReponseDto {
    private HttpStatus status;
    private String message;
}
