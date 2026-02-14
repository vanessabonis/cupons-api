package com.desafio.cupom.domain.exception;

import java.util.UUID;

public class CupomAlreadyDeletedException extends RuntimeException {
    public CupomAlreadyDeletedException(UUID id) {
        super(String.format("Cupom com ID %s já foi deletado", id));
    }
}

