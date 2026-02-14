package com.desafio.cupom.domain.exception;

public class ExpiredCupomException extends RuntimeException {
    public ExpiredCupomException(String message) {
        super(message);
    }
}

