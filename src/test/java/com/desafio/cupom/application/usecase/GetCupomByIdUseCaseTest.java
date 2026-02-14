package com.desafio.cupom.application.usecase;

import com.desafio.cupom.application.dto.CupomResponse;
import com.desafio.cupom.application.mapper.CupomMapper;
import com.desafio.cupom.domain.entity.Cupom;
import com.desafio.cupom.domain.enums.CupomStatus;
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
@DisplayName("GetCupomByIdUseCase - Testes Unitarios")
class GetCupomByIdUseCaseTest {

    private static final String CODIGO_TESTE = "ABC123";
    private static final String DESCRICAO_TESTE = "Teste";
    private static final BigDecimal DESCONTO_TESTE = new BigDecimal("10.00");

    @Mock
    private CupomRepository cupomRepository;

    @Mock
    private CupomMapper cupomMapper;

    @InjectMocks
    private GetCupomByIdUseCase useCase;

    @Test
    @DisplayName("Deve buscar cupom por ID com sucesso")
    void deveBuscarCupomPorIdComSucesso() {
        UUID id = UUID.randomUUID();
        Cupom cupom = Cupom.create(
            CODIGO_TESTE,
            DESCRICAO_TESTE,
            DESCONTO_TESTE,
            LocalDateTime.now().plusDays(30),
            false
        );

        CupomResponse expectedResponse = new CupomResponse(
            id,
            CODIGO_TESTE,
            DESCRICAO_TESTE,
            DESCONTO_TESTE,
            cupom.getExpirationDate(),
            CupomStatus.ACTIVE,
            false,
            false
        );

        when(cupomRepository.findById(id)).thenReturn(Optional.of(cupom));
        when(cupomMapper.toResponse(cupom)).thenReturn(expectedResponse);

        CupomResponse response = useCase.execute(id);

        assertThat(response).isNotNull();
        assertThat(response.id()).isEqualTo(id);
        verify(cupomRepository).findById(id);
        verify(cupomMapper).toResponse(cupom);
    }

    @Test
    @DisplayName("Deve lancar excecao quando cupom nao existe por ID")
    void deveLancarExcecaoQuandoCupomNaoExistePorId() {
        UUID id = UUID.randomUUID();

        when(cupomRepository.findById(id)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> useCase.execute(id))
            .isInstanceOf(CupomNotFoundException.class)
            .hasMessageContaining(id.toString());

        verify(cupomRepository).findById(id);
        verify(cupomMapper, never()).toResponse(any());
    }
}

