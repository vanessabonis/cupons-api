package com.desafio.cupom.domain.exception;

public class InvalidCupomCodeException extends RuntimeException {
    public InvalidCupomCodeException(String message) {
        super(message);
    }
}

