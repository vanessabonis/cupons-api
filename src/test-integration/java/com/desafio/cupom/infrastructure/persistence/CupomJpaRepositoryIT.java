package com.desafio.cupom.infrastructure.persistence;

import com.desafio.cupom.domain.entity.Cupom;
import com.desafio.cupom.domain.enums.CupomStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.test.context.ActiveProfiles;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("CupomJpaRepository - Testes de Integração")
class CupomJpaRepositoryIT {

    @Autowired
    private CupomJpaRepository repository;

    @Test
    @DisplayName("Deve salvar e recuperar cupom com sucesso")
    void deveSalvarERecuperarCupom() {
        Cupom cupom = Cupom.create("ABC123", "Teste", new BigDecimal("10.00"),
            LocalDateTime.now().plusDays(30), false);

        Cupom saved = repository.saveAndFlush(cupom);

        assertThat(saved.getId()).isNotNull();
        assertThat(saved.getCodeValue()).isEqualTo("ABC123");
        assertThat(saved.getCreatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Deve encontrar cupom por ID")
    void deveEncontrarCupomPorId() {
        Cupom cupom = Cupom.create("ABC123", "Teste", new BigDecimal("10.00"),
            LocalDateTime.now().plusDays(30), false);
        Cupom saved = repository.saveAndFlush(cupom);

        Optional<Cupom> found = repository.findById(saved.getId());

        assertThat(found).isPresent();
        assertThat(found.get().getCodeValue()).isEqualTo("ABC123");
    }

    @Test
    @DisplayName("Deve retornar vazio quando cupom não existe por ID")
    void deveRetornarVazioQuandoCupomNaoExistePorId() {
        Optional<Cupom> found = repository.findById(UUID.randomUUID());

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Deve encontrar cupom por código")
    void deveEncontrarCupomPorCodigo() {
        Cupom cupom = Cupom.create("ABC123", "Teste", new BigDecimal("10.00"),
            LocalDateTime.now().plusDays(30), false);
        repository.saveAndFlush(cupom);

        Optional<Cupom> found = repository.findByCode("ABC123");

        assertThat(found).isPresent();
        assertThat(found.get().getCodeValue()).isEqualTo("ABC123");
    }

    @Test
    @DisplayName("Deve retornar vazio quando cupom não existe por código")
    void deveRetornarVazioQuandoCupomNaoExistePorCodigo() {
        Optional<Cupom> found = repository.findByCode("NAOEXI");

        assertThat(found).isEmpty();
    }

    @Test
    @DisplayName("Deve verificar se cupom existe por código")
    void deveVerificarSeExistePorCodigo() {
        Cupom cupom = Cupom.create("ABC123", "Teste", new BigDecimal("10.00"),
            LocalDateTime.now().plusDays(30), false);
        repository.saveAndFlush(cupom);

        boolean exists = repository.existsByCode("ABC123");

        assertThat(exists).isTrue();
    }

    @Test
    @DisplayName("Deve retornar false quando cupom não existe por código")
    void deveRetornarFalseQuandoCupomNaoExistePorCodigo() {
        boolean exists = repository.existsByCode("NAOEXI");

        assertThat(exists).isFalse();
    }

    @Test
    @DisplayName("Deve persistir soft delete corretamente")
    void devePersistirSoftDelete() {
        Cupom cupom = Cupom.create("ABC123", "Teste", new BigDecimal("10.00"),
            LocalDateTime.now().plusDays(30), false);
        Cupom saved = repository.saveAndFlush(cupom);

        saved.delete();
        repository.saveAndFlush(saved);

        Cupom found = repository.findById(saved.getId()).orElseThrow();
        assertThat(found.getStatus()).isEqualTo(CupomStatus.DELETED);
        assertThat(found.getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("Deve persistir timestamps automaticamente")
    void devePersistirTimestamps() {
        Cupom cupom = Cupom.create("ABC123", "Teste", new BigDecimal("10.00"),
            LocalDateTime.now().plusDays(30), false);

        Cupom saved = repository.saveAndFlush(cupom);

        assertThat(saved.getCreatedAt()).isNotNull();
        assertThat(saved.getUpdatedAt()).isNotNull();
    }

    @Test
    @DisplayName("Deve respeitar unique constraint no código")
    void deveRespeitarUniqueConstraintNoCodigo() {
        Cupom cupom1 = Cupom.create("ABC123", "Teste 1", new BigDecimal("10.00"),
            LocalDateTime.now().plusDays(30), false);
        repository.saveAndFlush(cupom1);

        Cupom cupom2 = Cupom.create("ABC123", "Teste 2", new BigDecimal("20.00"),
            LocalDateTime.now().plusDays(30), false);

        assertThatThrownBy(() -> repository.saveAndFlush(cupom2))
            .isInstanceOf(DataIntegrityViolationException.class);
    }
}

