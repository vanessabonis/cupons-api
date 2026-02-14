package com.desafio.cupom.application.dto;

import com.desafio.cupom.domain.enums.CupomStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DisplayName("CupomResponse DTO - Testes")
class CupomResponseTest {

    private static final UUID ID_TESTE = UUID.randomUUID();
    private static final String CODIGO_TESTE = "ABC123";
    private static final String DESCRICAO_TESTE = "Teste de cupom";
    private static final BigDecimal DESCONTO_TESTE = new BigDecimal("10.00");

    @Test
    @DisplayName("Deve criar CupomResponse com todos os campos")
    void deveCriarCupomResponseComTodosCampos() {
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(30);

        CupomResponse response = new CupomResponse(
            ID_TESTE,
            CODIGO_TESTE,
            DESCRICAO_TESTE,
            DESCONTO_TESTE,
            expirationDate,
            CupomStatus.ACTIVE,
            true,
            false
        );

        assertThat(response.id()).isEqualTo(ID_TESTE);
        assertThat(response.code()).isEqualTo(CODIGO_TESTE);
        assertThat(response.description()).isEqualTo(DESCRICAO_TESTE);
        assertThat(response.discountValue()).isEqualByComparingTo(DESCONTO_TESTE);
        assertThat(response.expirationDate()).isEqualTo(expirationDate);
        assertThat(response.status()).isEqualTo(CupomStatus.ACTIVE);
        assertThat(response.published()).isTrue();
        assertThat(response.redeemed()).isFalse();
    }

    @Test
    @DisplayName("Deve implementar equals e hashCode corretamente")
    void deveImplementarEqualsEHashCode() {
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(30);

        CupomResponse response1 = new CupomResponse(ID_TESTE, CODIGO_TESTE, DESCRICAO_TESTE,
            DESCONTO_TESTE, expirationDate, CupomStatus.ACTIVE, true, false);
        CupomResponse response2 = new CupomResponse(ID_TESTE, CODIGO_TESTE, DESCRICAO_TESTE,
            DESCONTO_TESTE, expirationDate, CupomStatus.ACTIVE, true, false);
        CupomResponse response3 = new CupomResponse(UUID.randomUUID(), "XYZ999", DESCRICAO_TESTE,
            DESCONTO_TESTE, expirationDate, CupomStatus.ACTIVE, true, false);

        assertThat(response1)
            .isEqualTo(response2)
            .hasSameHashCodeAs(response2)
            .isNotEqualTo(response3);
    }

    @Test
    @DisplayName("Deve ter toString válido")
    void deveTerToStringValido() {
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(30);

        CupomResponse response = new CupomResponse(
            ID_TESTE,
            CODIGO_TESTE,
            DESCRICAO_TESTE,
            DESCONTO_TESTE,
            expirationDate,
            CupomStatus.ACTIVE,
            true,
            false
        );

        String toString = response.toString();

        assertThat(toString)
            .contains(ID_TESTE.toString())
            .contains(CODIGO_TESTE)
            .contains(DESCRICAO_TESTE)
            .contains("ACTIVE");
    }
}

