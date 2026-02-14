package com.desafio.cupom.domain.value;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;

import static org.assertj.core.api.Assertions.*;

@DisplayName("DiscountValue Value Object - Testes")
class DiscountValueTest {

    @ParameterizedTest
    @ValueSource(strings = {"0.5", "10.00", "999.99"})
    @DisplayName("Deve criar desconto com valores válidos")
    void deveCriarDescontoValido(String amount) {
        DiscountValue discount = DiscountValue.of(new BigDecimal(amount));

        assertThat(discount).isNotNull();
        assertThat(discount.getValue()).isEqualByComparingTo(new BigDecimal(amount));
    }

    @Test
    @DisplayName("Deve lançar exceção quando valor é nulo")
    void deveLancarExcecaoQuandoValorNulo() {
        BigDecimal valorNulo = null;

        assertThatThrownBy(() -> DiscountValue.of(valorNulo))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("não pode ser nulo");
    }

    @Test
    @DisplayName("Deve lançar exceção quando valor é zero")
    void deveLancarExcecaoQuandoValorZero() {
        BigDecimal zero = BigDecimal.ZERO;

        assertThatThrownBy(() -> DiscountValue.of(zero))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("positivo");
    }

    @Test
    @DisplayName("Deve lançar exceção quando valor é negativo")
    void deveLancarExcecaoQuandoValorNegativo() {
        BigDecimal negativo = new BigDecimal("-10.00");

        assertThatThrownBy(() -> DiscountValue.of(negativo))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("positivo");
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.49", "0.3", "0.1"})
    @DisplayName("Deve lançar exceção quando valor é menor que 0.5 mas positivo")
    void deveLancarExcecaoQuandoValorMenorQueMinimo(String amount) {
        BigDecimal value = new BigDecimal(amount);

        assertThatThrownBy(() -> DiscountValue.of(value))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining(">= 0.5");
    }


    @Test
    @DisplayName("Deve implementar equals e hashCode corretamente")
    void deveImplementarEqualsEHashCode() {
        DiscountValue discount1 = DiscountValue.of(new BigDecimal("10.00"));
        DiscountValue discount2 = DiscountValue.of(new BigDecimal("10.00"));
        DiscountValue discount3 = DiscountValue.of(new BigDecimal("20.00"));

        assertThat(discount1)
            .isEqualTo(discount2)
            .hasSameHashCodeAs(discount2)
            .isNotEqualTo(discount3);
    }
}

