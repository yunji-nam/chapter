package com.example.chapter.exception;

public class DuplicateFieldException extends RuntimeException {

    public DuplicateFieldException(String fieldName, String fieldValue) {
        super("이미 존재하는 " + fieldName + "입니다: " + fieldValue);
    }
}
