package com.example.chapter.exception;

public class PaymentAmountMismatchException extends RuntimeException{

    public PaymentAmountMismatchException(String message) {
            super(message);
        }
}
