package com.desafio.cupom.domain.entity;

import com.desafio.cupom.domain.enums.CupomStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.NullSource;
import org.junit.jupiter.params.provider.ValueSource;

import java.math.BigDecimal;
import java.time.LocalDateTime;

import static org.assertj.core.api.Assertions.*;

@DisplayName("Cupom Entity - Testes")
class CupomTest {

    private static final String CODIGO_VALIDO = "ABC123";
    private static final String CODIGO_COM_ESPECIAIS = "ABC-123";
    private static final String CODIGO_INVALIDO = "ABC";
    private static final String DESCRICAO_TESTE = "Teste";
    private static final BigDecimal DESCONTO_VALIDO = new BigDecimal("10");
    private static final BigDecimal DESCONTO_VALIDO_DETALHADO = new BigDecimal("10.00");
    private static final BigDecimal DESCONTO_INVALIDO = new BigDecimal("0.3");
    private static final BigDecimal DESCONTO_MINIMO = new BigDecimal("0.5");
    private static final BigDecimal DESCONTO_MAXIMO = new BigDecimal("999.99");
    private static final int DIAS_FUTURO = 30;
    private static final int UM_DIA = 1;

    // Métodos auxiliares para criar objetos de teste
    private Cupom createCupomValido() {
        return Cupom.create(
            CODIGO_VALIDO,
            DESCRICAO_TESTE,
            DESCONTO_VALIDO,
            LocalDateTime.now().plusDays(DIAS_FUTURO),
            false
        );
    }

    private LocalDateTime getDataFutura() {
        return LocalDateTime.now().plusDays(UM_DIA);
    }

    private LocalDateTime getDataPassado() {
        return LocalDateTime.now().minusDays(UM_DIA);
    }

    @Test
    @DisplayName("Deve criar cupom válido com valores corretos")
    void deveCriarCupomValido() {
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(DIAS_FUTURO);

        Cupom cupom = Cupom.create(CODIGO_COM_ESPECIAIS, DESCRICAO_TESTE, DESCONTO_VALIDO_DETALHADO, expirationDate, true);

        assertThat(cupom.getCodeValue()).isEqualTo(CODIGO_VALIDO);
        assertThat(cupom.getDescription()).isEqualTo(DESCRICAO_TESTE);
        assertThat(cupom.getDiscountValueAmount()).isEqualByComparingTo(DESCONTO_VALIDO_DETALHADO);
        assertThat(cupom.getExpirationDate()).isEqualTo(expirationDate);
        assertThat(cupom)
            .extracting(Cupom::getPublished, Cupom::getRedeemed, Cupom::getStatus,
                       Cupom::isActive, Cupom::isDeleted, Cupom::getDeletedAt)
            .containsExactly(true, false, CupomStatus.ACTIVE, true, false, null);
    }

    @ParameterizedTest
    @NullSource
    @ValueSource(booleans = {false})
    @DisplayName("Deve usar published=false quando null ou false")
    void deveUsarPublishedFalse(Boolean published) {
        Cupom cupom = Cupom.create(CODIGO_VALIDO, DESCRICAO_TESTE, DESCONTO_VALIDO,
            getDataFutura(), published);

        assertThat(cupom.getPublished()).isFalse();
    }

    @Test
    @DisplayName("Deve lançar exceções quando campos inválidos")
    void deveLancarExcecoesQuandoCamposInvalidos() {
        LocalDateTime futureDate = getDataFutura();

        // Código inválido
        assertThatThrownBy(() -> Cupom.create(CODIGO_INVALIDO, DESCRICAO_TESTE, DESCONTO_VALIDO, futureDate, false))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("6 caracteres");

        // Desconto inválido
        assertThatThrownBy(() -> Cupom.create(CODIGO_VALIDO, DESCRICAO_TESTE, DESCONTO_INVALIDO, futureDate, false))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("0.5");

        // Data nula
        LocalDateTime dataNula = null;
        assertThatThrownBy(() -> Cupom.create(CODIGO_VALIDO, DESCRICAO_TESTE, DESCONTO_VALIDO, dataNula, false))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("Data de expiração não pode ser nula");

        // Data no passado
        assertThatThrownBy(() -> Cupom.create(CODIGO_VALIDO, DESCRICAO_TESTE, DESCONTO_VALIDO, getDataPassado(), false))
            .isInstanceOf(IllegalArgumentException.class)
            .hasMessageContaining("não pode estar no passado");
    }

    @Test
    @DisplayName("Deve deletar cupom com sucesso (soft delete)")
    void deveDeletarCupomComSucesso() {
        Cupom cupom = createCupomValido();

        cupom.delete();

        assertThat(cupom)
            .extracting(Cupom::getStatus, Cupom::isDeleted, Cupom::isActive)
            .containsExactly(CupomStatus.DELETED, true, false);
        assertThat(cupom.getDeletedAt()).isNotNull();
    }

    @Test
    @DisplayName("Deve lançar exceção ao deletar cupom já deletado")
    void deveLancarExcecaoAoDeletarCupomJaDeletado() {
        Cupom cupom = createCupomValido();
        cupom.delete();

        assertThatThrownBy(cupom::delete)
            .isInstanceOf(IllegalStateException.class)
            .hasMessageContaining("já foi deletado");
    }

    @ParameterizedTest
    @ValueSource(strings = {"0.5", "999.99"})
    @DisplayName("Deve aceitar valores de desconto válidos (mínimo e máximo)")
    void deveAceitarValoresDescontoValidos(String discount) {
        BigDecimal descontoTeste = new BigDecimal(discount);

        Cupom cupom = Cupom.create(CODIGO_VALIDO, DESCRICAO_TESTE, descontoTeste, getDataFutura(), false);

        assertThat(cupom.getDiscountValueAmount()).isEqualByComparingTo(descontoTeste);
    }
}

