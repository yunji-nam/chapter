package com.example.chapter.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;

@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ErrorResponse {

    private String msg;
    private int status;

    public ErrorResponse(String msg, HttpStatus status) {
        this.msg = msg;
        this.status = status.value();
    }
}
