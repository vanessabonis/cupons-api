package com.desafio.cupom.application.usecase;

import com.desafio.cupom.domain.entity.Cupom;
import com.desafio.cupom.domain.exception.CupomNotFoundException;
import com.desafio.cupom.domain.repository.CupomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("DeleteCupomUseCase - Testes Unitarios")
class DeleteCupomUseCaseTest {

    private static final String CODIGO_TESTE = "ABC123";
    private static final String DESCRICAO_TESTE = "Teste";
    private static final BigDecimal DESCONTO_TESTE = new BigDecimal("10.00");

    @Mock
    private CupomRepository cupomRepository;

    @InjectMocks
    private DeleteCupomUseCase useCase;

    @Test
    @DisplayName("Deve deletar cupom com sucesso")
    void deveDeletarCupomComSucesso() {
        UUID id = UUID.randomUUID();
        Cupom cupom = Cupom.create(
            CODIGO_TESTE,
            DESCRICAO_TESTE,
            DESCONTO_TESTE,
            LocalDateTime.now().plusDays(30),
            false
        );

        when(cupomRepository.findById(id)).thenReturn(Optional.of(cupom));
        when(cupomRepository.save(cupom)).thenReturn(cupom);

        useCase.execute(id);

        assertThat(cupom.isDeleted()).isTrue();
        verify(cupomRepository).findById(id);
        verify(cupomRepository).save(cupom);
    }

    @Test
    @DisplayName("Deve lancar excecao ao deletar cupom inexistente")
    void deveLancarExcecaoAoDeletarCupomInexistente() {
        UUID id = UUID.randomUUID();

        when(cupomRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(id))
            .isInstanceOf(CupomNotFoundException.class)
            .hasMessageContaining(id.toString());

        verify(cupomRepository).findById(id);
        verify(cupomRepository, never()).save(any());
    }
}

