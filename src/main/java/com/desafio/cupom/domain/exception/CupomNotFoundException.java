package com.desafio.cupom.domain.exception;

import java.util.UUID;

public class CupomNotFoundException extends RuntimeException {
    public CupomNotFoundException(UUID id) {
        super(String.format("Cupom com ID %s não encontrado", id));
    }
}

