package com.desafio.cupom.domain.value;

import jakarta.persistence.Embeddable;
import lombok.AccessLevel;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Getter
@EqualsAndHashCode
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Code implements Serializable {

    private static final int REQUIRED_LENGTH = 6;
    private static final String REGEX_SPECIAL_CHARS = "[^a-zA-Z0-9]";

    private String value;

    private Code(String value) {
        this.value = value;
    }

    public static Code of(String rawCode) {
        if (rawCode == null || rawCode.isBlank()) {
            throw new IllegalArgumentException("Código não pode ser nulo ou vazio");
        }

        // Remove caracteres especiais e converte para uppercase
        String cleaned = rawCode.replaceAll(REGEX_SPECIAL_CHARS, "").toUpperCase();

        // Valida tamanho após limpeza
        if (cleaned.length() != REQUIRED_LENGTH) {
            throw new IllegalArgumentException(
                String.format("Código deve ter %d caracteres alfanuméricos após limpeza. Recebido: %s (%d caracteres)",
                    REQUIRED_LENGTH, cleaned, cleaned.length())
            );
        }

        return new Code(cleaned);
    }

    @Override
    public String toString() {
        return value;
    }
}

