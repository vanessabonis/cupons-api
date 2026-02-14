package com.desafio.cupom.application.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record CupomRequest(

    @NotBlank(message = "Código não pode ser vazio")
    String code,

    @NotBlank(message = "Descrição não pode ser vazia")
    String description,

    @NotNull(message = "Valor de desconto não pode ser nulo")
    @DecimalMin(value = "0.5", message = "Valor de desconto deve ser >= 0.5")
    BigDecimal discountValue,

    @NotNull(message = "Data de expiração não pode ser nula")
    @Future(message = "Data de expiração deve estar no futuro")
    LocalDateTime expirationDate,

    @JsonProperty(required = false)
    Boolean published
) {
}

