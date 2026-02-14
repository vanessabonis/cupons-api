package com.desafio.cupom.application.usecase;

import com.desafio.cupom.application.dto.CupomRequest;
import com.desafio.cupom.application.dto.CupomResponse;
import com.desafio.cupom.application.mapper.CupomMapper;
import com.desafio.cupom.domain.entity.Cupom;
import com.desafio.cupom.domain.enums.CupomStatus;
import com.desafio.cupom.domain.repository.CupomRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateCupomUseCase - Testes Unitarios")
class CreateCupomUseCaseTest {

    private static final String CODIGO_TESTE = "ABC123";
    private static final String DESCRICAO_TESTE = "Teste";
    private static final BigDecimal DESCONTO_TESTE = new BigDecimal("10.00");
    private static final int DIAS_EXPIRACAO = 30;

    @Mock
    private CupomRepository cupomRepository;

    @Mock
    private CupomMapper cupomMapper;

    @InjectMocks
    private CreateCupomUseCase useCase;

    @Test
    @DisplayName("Deve criar cupom com sucesso")
    void deveCriarCupomComSucesso() {
        CupomRequest request = new CupomRequest(
            CODIGO_TESTE,
            DESCRICAO_TESTE,
            DESCONTO_TESTE,
            LocalDateTime.now().plusDays(DIAS_EXPIRACAO),
            false
        );

        Cupom cupom = Cupom.create(
            request.code(),
            request.description(),
            request.discountValue(),
            request.expirationDate(),
            request.published()
        );

        CupomResponse expectedResponse = new CupomResponse(
            UUID.randomUUID(),
            CODIGO_TESTE,
            DESCRICAO_TESTE,
            DESCONTO_TESTE,
            request.expirationDate(),
            CupomStatus.ACTIVE,
            false,
            false
        );

        when(cupomRepository.save(any(Cupom.class))).thenReturn(cupom);
        when(cupomMapper.toResponse(cupom)).thenReturn(expectedResponse);

        CupomResponse response = useCase.execute(request);

        assertThat(response).isNotNull();
        assertThat(response.code()).isEqualTo(CODIGO_TESTE);
        verify(cupomRepository).save(any(Cupom.class));
        verify(cupomMapper).toResponse(any(Cupom.class));
    }
}

