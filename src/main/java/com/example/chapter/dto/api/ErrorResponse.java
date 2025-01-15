package com.example.chapter.dto.api;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private String message;
    private int status;

    public ErrorResponse(String message, HttpStatus status) {
        this.message = message;
        this.status = status.value();
    }
}
