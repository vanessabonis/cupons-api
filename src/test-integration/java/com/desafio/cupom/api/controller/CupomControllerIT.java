package com.desafio.cupom.api.controller;

import com.desafio.cupom.application.dto.CupomRequest;
import com.desafio.cupom.domain.entity.Cupom;
import com.desafio.cupom.domain.enums.CupomStatus;
import com.desafio.cupom.infrastructure.persistence.CupomJpaRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
@Transactional
@DisplayName("CupomController - Testes de Integração")
class CupomControllerIT {

    private static final String BASE_URL = "/coupon";
    private static final String CODIGO_TESTE = "ABC123";
    private static final String DESCRICAO_TESTE = "Cupom de teste";
    private static final BigDecimal DESCONTO_TESTE = new BigDecimal("10.00");

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CupomJpaRepository repository;

    @Test
    @DisplayName("POST /coupon - Deve criar cupom com sucesso")
    void deveCriarCupomComSucesso() throws Exception {
        CupomRequest request = new CupomRequest(
            CODIGO_TESTE,
            DESCRICAO_TESTE,
            DESCONTO_TESTE,
            LocalDateTime.now().plusDays(30),
            false
        );

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.id").exists())
            .andExpect(jsonPath("$.code").value(CODIGO_TESTE))
            .andExpect(jsonPath("$.description").value(DESCRICAO_TESTE))
            .andExpect(jsonPath("$.discountValue").value(10.00))
            .andExpect(jsonPath("$.status").value("ACTIVE"))
            .andExpect(jsonPath("$.published").value(false))
            .andExpect(jsonPath("$.redeemed").value(false));
    }

    @Test
    @DisplayName("POST /coupon - Deve retornar 400 quando código inválido")
    void deveRetornar400QuandoCodigoInvalido() throws Exception {
        CupomRequest request = new CupomRequest(
            "ABC",
            DESCRICAO_TESTE,
            DESCONTO_TESTE,
            LocalDateTime.now().plusDays(30),
            false
        );

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Bad Request"))
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @DisplayName("POST /coupon - Deve retornar 400 quando desconto menor que 0.5")
    void deveRetornar400QuandoDescontoInvalido() throws Exception {
        CupomRequest request = new CupomRequest(
            CODIGO_TESTE,
            DESCRICAO_TESTE,
            new BigDecimal("0.3"),
            LocalDateTime.now().plusDays(30),
            false
        );

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Validation Failed"))
            .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    @DisplayName("POST /coupon - Deve retornar 400 quando data no passado")
    void deveRetornar400QuandoDataNoPassado() throws Exception {
        CupomRequest request = new CupomRequest(
            CODIGO_TESTE,
            DESCRICAO_TESTE,
            DESCONTO_TESTE,
            LocalDateTime.now().minusDays(1),
            false
        );

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Validation Failed"))
            .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    @DisplayName("POST /coupon - Deve retornar 400 quando campos obrigatórios ausentes")
    void deveRetornar400QuandoCamposObrigatoriosAusentes() throws Exception {
        String requestJson = "{}";

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(requestJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.error").value("Validation Failed"))
            .andExpect(jsonPath("$.details").isArray());
    }

    @Test
    @DisplayName("GET /coupon/{id} - Deve retornar cupom existente")
    void deveRetornarCupomExistente() throws Exception {
        Cupom cupom = Cupom.create(CODIGO_TESTE, DESCRICAO_TESTE, DESCONTO_TESTE,
            LocalDateTime.now().plusDays(30), true);
        Cupom saved = repository.save(cupom);

        mockMvc.perform(get(BASE_URL + "/{id}", saved.getId()))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.id").value(saved.getId().toString()))
            .andExpect(jsonPath("$.code").value(CODIGO_TESTE))
            .andExpect(jsonPath("$.description").value(DESCRICAO_TESTE))
            .andExpect(jsonPath("$.status").value("ACTIVE"))
            .andExpect(jsonPath("$.published").value(true));
    }

    @Test
    @DisplayName("GET /coupon/{id} - Deve retornar 404 quando cupom não existe")
    void deveRetornar404QuandoCupomNaoExiste() throws Exception {
        UUID idInexistente = UUID.randomUUID();

        mockMvc.perform(get(BASE_URL + "/{id}", idInexistente))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Not Found"))
            .andExpect(jsonPath("$.message").value(containsString(idInexistente.toString())));
    }

    @Test
    @DisplayName("DELETE /coupon/{id} - Deve deletar cupom com sucesso")
    void deveDeletarCupomComSucesso() throws Exception {
        Cupom cupom = Cupom.create(CODIGO_TESTE, DESCRICAO_TESTE, DESCONTO_TESTE,
            LocalDateTime.now().plusDays(30), false);
        Cupom saved = repository.save(cupom);

        mockMvc.perform(delete(BASE_URL + "/{id}", saved.getId()))
            .andExpect(status().isNoContent());

        Cupom deleted = repository.findById(saved.getId()).orElseThrow();
        assert deleted.getStatus() == CupomStatus.DELETED;
        assert deleted.getDeletedAt() != null;
    }

    @Test
    @DisplayName("DELETE /coupon/{id} - Deve retornar 404 quando cupom não existe")
    void deveRetornar404AoDeletarCupomInexistente() throws Exception {
        UUID idInexistente = UUID.randomUUID();

        mockMvc.perform(delete(BASE_URL + "/{id}", idInexistente))
            .andExpect(status().isNotFound())
            .andExpect(jsonPath("$.error").value("Not Found"));
    }

    @Test
    @DisplayName("DELETE /coupon/{id} - Deve retornar 409 ao deletar cupom já deletado")
    void deveRetornar409AoDeletarCupomJaDeletado() throws Exception {
        Cupom cupom = Cupom.create(CODIGO_TESTE, DESCRICAO_TESTE, DESCONTO_TESTE,
            LocalDateTime.now().plusDays(30), false);
        cupom.delete();
        Cupom saved = repository.save(cupom);

        mockMvc.perform(delete(BASE_URL + "/{id}", saved.getId()))
            .andExpect(status().isConflict())
            .andExpect(jsonPath("$.error").value("Conflict"))
            .andExpect(jsonPath("$.message").value(containsString("deletado")));
    }

    @Test
    @DisplayName("POST /coupon - Deve criar cupom com código contendo caracteres especiais")
    void deveCriarCupomComCodigoEspecial() throws Exception {
        CupomRequest request = new CupomRequest(
            "ABC-123",
            DESCRICAO_TESTE,
            DESCONTO_TESTE,
            LocalDateTime.now().plusDays(30),
            true
        );

        mockMvc.perform(post(BASE_URL)
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(request)))
            .andExpect(status().isCreated())
            .andExpect(jsonPath("$.code").value(CODIGO_TESTE))
            .andExpect(jsonPath("$.published").value(true));
    }
}

