package com.desafio.cupom.application.dto;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CupomRequest DTO - Testes")
class CupomRequestTest {

    private static final String CODIGO_TESTE = "ABC123";
    private static final String DESCRICAO_TESTE = "Teste de cupom";
    private static final BigDecimal DESCONTO_TESTE = new BigDecimal("10.00");

    @Test
    @DisplayName("Deve criar CupomRequest com todos os campos")
    void deveCriarCupomRequestComTodosCampos() {
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(30);

        CupomRequest request = new CupomRequest(
            CODIGO_TESTE,
            DESCRICAO_TESTE,
            DESCONTO_TESTE,
            expirationDate,
            true
        );

        assertThat(request.code()).isEqualTo(CODIGO_TESTE);
        assertThat(request.description()).isEqualTo(DESCRICAO_TESTE);
        assertThat(request.discountValue()).isEqualByComparingTo(DESCONTO_TESTE);
        assertThat(request.expirationDate()).isEqualTo(expirationDate);
        assertThat(request.published()).isTrue();
    }

    @Test
    @DisplayName("Deve criar CupomRequest com published null")
    void deveCriarCupomRequestComPublishedNull() {
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(30);

        CupomRequest request = new CupomRequest(
            CODIGO_TESTE,
            DESCRICAO_TESTE,
            DESCONTO_TESTE,
            expirationDate,
            null
        );

        assertThat(request.published()).isNull();
    }

    @Test
    @DisplayName("Deve implementar equals e hashCode corretamente")
    void deveImplementarEqualsEHashCode() {
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(30);

        CupomRequest request1 = new CupomRequest(CODIGO_TESTE, DESCRICAO_TESTE, DESCONTO_TESTE, expirationDate, true);
        CupomRequest request2 = new CupomRequest(CODIGO_TESTE, DESCRICAO_TESTE, DESCONTO_TESTE, expirationDate, true);
        CupomRequest request3 = new CupomRequest("XYZ999", DESCRICAO_TESTE, DESCONTO_TESTE, expirationDate, true);

        assertThat(request1)
            .isEqualTo(request2)
            .hasSameHashCodeAs(request2)
            .isNotEqualTo(request3);
    }

    @Test
    @DisplayName("Deve ter toString válido")
    void deveTerToStringValido() {
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(30);

        CupomRequest request = new CupomRequest(
            CODIGO_TESTE,
            DESCRICAO_TESTE,
            DESCONTO_TESTE,
            expirationDate,
            true
        );

        String toString = request.toString();

        assertThat(toString)
            .contains(CODIGO_TESTE)
            .contains(DESCRICAO_TESTE)
            .contains("10.00");
    }
}

