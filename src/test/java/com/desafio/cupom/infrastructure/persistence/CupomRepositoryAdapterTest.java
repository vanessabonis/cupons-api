package com.desafio.cupom.infrastructure.persistence;

import com.desafio.cupom.domain.entity.Cupom;
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
@DisplayName("CupomRepositoryAdapter - Testes Unitários")
class CupomRepositoryAdapterTest {

    @Mock
    private CupomJpaRepository jpaRepository;

    @InjectMocks
    private CupomRepositoryAdapter adapter;

    @Test
    @DisplayName("Deve delegar save para JpaRepository")
    void deveDelegarSave() {
        Cupom cupom = Cupom.create("ABC123", "Teste", new BigDecimal("10.00"),
            LocalDateTime.now().plusDays(30), false);

        when(jpaRepository.save(cupom)).thenReturn(cupom);

        Cupom result = adapter.save(cupom);

        assertThat(result).isEqualTo(cupom);
        verify(jpaRepository).save(cupom);
    }

    @Test
    @DisplayName("Deve delegar findById para JpaRepository")
    void deveDelegarFindById() {
        UUID id = UUID.randomUUID();
        Cupom cupom = Cupom.create("ABC123", "Teste", new BigDecimal("10.00"),
            LocalDateTime.now().plusDays(30), false);

        when(jpaRepository.findById(id)).thenReturn(Optional.of(cupom));

        Optional<Cupom> result = adapter.findById(id);

        assertThat(result).isPresent();
        verify(jpaRepository).findById(id);
    }

    @Test
    @DisplayName("Deve delegar findByCode para JpaRepository")
    void deveDelegarFindByCode() {
        String code = "ABC123";
        Cupom cupom = Cupom.create(code, "Teste", new BigDecimal("10.00"),
            LocalDateTime.now().plusDays(30), false);

        when(jpaRepository.findByCode(code)).thenReturn(Optional.of(cupom));

        Optional<Cupom> result = adapter.findByCode(code);

        assertThat(result).isPresent();
        verify(jpaRepository).findByCode(code);
    }

    @Test
    @DisplayName("Deve delegar existsByCode para JpaRepository")
    void deveDelegarExistsByCode() {
        String code = "ABC123";

        when(jpaRepository.existsByCode(code)).thenReturn(true);

        boolean result = adapter.existsByCode(code);

        assertThat(result).isTrue();
        verify(jpaRepository).existsByCode(code);
    }
}

