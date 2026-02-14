package com.desafio.cupom.domain.value;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullAndEmptySource;
import org.junit.jupiter.params.provider.ValueSource;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Code Value Object - Testes")
class CodeTest {

    @Test
    @DisplayName("Deve criar código válido e remover caracteres especiais e converter uppercase")
    void deveCriarCodigoValidoComTransformacoes() {
        assertThat(Code.of("ABC123").getValue()).isEqualTo("ABC123");
        assertThat(Code.of("ABC-123").getValue()).isEqualTo("ABC123");
        assertThat(Code.of("abc123").getValue()).isEqualTo("ABC123");
        assertThat(Code.of("a@b#c$123").getValue()).isEqualTo("ABC123");
    }

    @ParameterizedTest
    @NullAndEmptySource
    @ValueSource(strings = {"   "})
    @DisplayName("Deve lançar exceção quando código é nulo, vazio ou apenas espaços")
    void deveLancarExcecaoQuandoCodigoInvalido(String rawCode) {
        assertThatThrownBy(() -> Code.of(rawCode))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Código não pode ser nulo ou vazio");
    }

    @ParameterizedTest
    @ValueSource(strings = {"ABC", "A", "12345"})
    @DisplayName("Deve lançar exceção quando código tem menos de 6 caracteres")
    void deveLancarExcecaoQuandoCodigoMenorQue6(String rawCode) {
        assertThatThrownBy(() -> Code.of(rawCode))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("6 caracteres");
    }

    @Test
    @DisplayName("Deve lançar exceção quando código tem mais de 6 caracteres")
    void deveLancarExcecaoQuandoCodigoMaiorQue6() {
        String codigoGrande = "ABCDEFG";

        assertThatThrownBy(() -> Code.of(codigoGrande))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("7 caracteres");
    }

    @Test
    @DisplayName("Deve implementar equals e hashCode corretamente")
    void deveImplementarEqualsEHashCode() {
        Code code1 = Code.of("ABC123");
        Code code2 = Code.of("ABC123");
        Code code3 = Code.of("XYZ999");

        assertThat(code1)
            .isEqualTo(code2)
            .hasSameHashCodeAs(code2)
            .isNotEqualTo(code3);
    }
}

