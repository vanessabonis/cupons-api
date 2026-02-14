package com.desafio.cupom.domain.value;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DiscountValue implements Serializable {

    private static final BigDecimal MIN_VALUE = new BigDecimal("0.5");

    private BigDecimal value;

    private DiscountValue(BigDecimal value) {
        this.value = value;
    }

    public static DiscountValue of(BigDecimal amount) {
        if (amount == null) {
            throw new IllegalArgumentException("Valor de desconto não pode ser nulo");
        }

        if (amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Valor de desconto deve ser positivo");
        }

        if (amount.compareTo(MIN_VALUE) < 0) {
            throw new IllegalArgumentException(
                String.format("Valor de desconto deve ser >= %s. Recebido: %s", MIN_VALUE, amount)
            );
        }


        return new DiscountValue(amount);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}

