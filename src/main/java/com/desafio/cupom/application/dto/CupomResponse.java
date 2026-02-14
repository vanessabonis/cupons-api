package com.desafio.cupom.application.dto;

import com.desafio.cupom.domain.enums.CupomStatus;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public record CupomResponse(
    UUID id,
    String code,
    String description,
    BigDecimal discountValue,
    LocalDateTime expirationDate,
    CupomStatus status,
    Boolean published,
    Boolean redeemed
) {
}

