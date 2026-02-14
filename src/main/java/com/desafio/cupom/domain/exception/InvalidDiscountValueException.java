package com.desafio.cupom.domain.exception;

public class InvalidDiscountValueException extends RuntimeException {
    public InvalidDiscountValueException(String message) {
        super(message);
    }
}

