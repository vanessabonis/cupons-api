package com.desafio.cupom.domain.exception;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Domain Exceptions - Testes")
class DomainExceptionsTest {

    @Test
    @DisplayName("InvalidCupomCodeException deve conter mensagem")
    void invalidCupomCodeExceptionDeveTerMensagem() {
        String mensagem = "Código inválido";

        InvalidCupomCodeException exception = new InvalidCupomCodeException(mensagem);

        assertThat(exception)
            .isInstanceOf(RuntimeException.class)
            .hasMessage(mensagem);
    }

    @Test
    @DisplayName("InvalidDiscountValueException deve conter mensagem")
    void invalidDiscountValueExceptionDeveTerMensagem() {
        String mensagem = "Desconto inválido";

        InvalidDiscountValueException exception = new InvalidDiscountValueException(mensagem);

        assertThat(exception)
            .isInstanceOf(RuntimeException.class)
            .hasMessage(mensagem);
    }

    @Test
    @DisplayName("ExpiredCupomException deve conter mensagem")
    void expiredCupomExceptionDeveTerMensagem() {
        String mensagem = "Cupom expirado";

        ExpiredCupomException exception = new ExpiredCupomException(mensagem);

        assertThat(exception)
            .isInstanceOf(RuntimeException.class)
            .hasMessage(mensagem);
    }

    @Test
    @DisplayName("CupomNotFoundException deve conter ID na mensagem")
    void cupomNotFoundExceptionDeveTerIdNaMensagem() {
        UUID id = UUID.randomUUID();

        CupomNotFoundException exception = new CupomNotFoundException(id);

        assertThat(exception)
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining(id.toString())
            .hasMessageContaining("não encontrado");
    }

    @Test
    @DisplayName("CupomAlreadyDeletedException deve conter ID na mensagem")
    void cupomAlreadyDeletedExceptionDeveTerIdNaMensagem() {
        UUID id = UUID.randomUUID();

        CupomAlreadyDeletedException exception = new CupomAlreadyDeletedException(id);

        assertThat(exception)
            .isInstanceOf(RuntimeException.class)
            .hasMessageContaining(id.toString())
            .hasMessageContaining("já foi deletado");
    }
}

