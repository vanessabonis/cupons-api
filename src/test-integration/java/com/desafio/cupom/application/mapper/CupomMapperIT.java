package com.desafio.cupom.application.mapper;
import com.desafio.cupom.application.dto.CupomResponse;
import com.desafio.cupom.domain.entity.Cupom;
import com.desafio.cupom.domain.enums.CupomStatus;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
@ActiveProfiles("test")
@DisplayName("CupomMapper - Testes de Integração")
class CupomMapperIT {
    private static final String CODIGO_TESTE = "ABC123";
    private static final String DESCRICAO_TESTE = "Teste de cupom";
    private static final BigDecimal DESCONTO_TESTE = new BigDecimal("10.00");
    @Autowired
    private CupomMapper cupomMapper;
    @Test
    @DisplayName("Deve mapear Cupom para CupomResponse corretamente")
    void deveMapearCupomParaResponse() {
        LocalDateTime expirationDate = LocalDateTime.now().plusDays(30);
        Cupom cupom = Cupom.create(CODIGO_TESTE, DESCRICAO_TESTE, DESCONTO_TESTE, expirationDate, true);
        CupomResponse response = cupomMapper.toResponse(cupom);
        assertThat(response).isNotNull();
        assertThat(response.code()).isEqualTo(CODIGO_TESTE);
        assertThat(response.description()).isEqualTo(DESCRICAO_TESTE);
        assertThat(response.discountValue()).isEqualByComparingTo(DESCONTO_TESTE);
        assertThat(response.expirationDate()).isEqualTo(expirationDate);
        assertThat(response.status()).isEqualTo(CupomStatus.ACTIVE);
        assertThat(response.published()).isTrue();
        assertThat(response.redeemed()).isFalse();
    }
    @Test
    @DisplayName("Deve mapear campos embedded corretamente")
    void deveMapearCamposEmbedded() {
        Cupom cupom = Cupom.create("XYZ-999", "Outro teste", new BigDecimal("50.00"), 
            LocalDateTime.now().plusDays(15), false);
        CupomResponse response = cupomMapper.toResponse(cupom);
        assertThat(response.code()).isEqualTo("XYZ999");
        assertThat(response.discountValue()).isEqualByComparingTo(new BigDecimal("50.00"));
    }
    @Test
    @DisplayName("Deve mapear cupom deletado corretamente")
    void deveMapearCupomDeletado() {
        Cupom cupom = Cupom.create(CODIGO_TESTE, DESCRICAO_TESTE, DESCONTO_TESTE, 
            LocalDateTime.now().plusDays(30), false);
        cupom.delete();
        CupomResponse response = cupomMapper.toResponse(cupom);
        assertThat(response.status()).isEqualTo(CupomStatus.DELETED);
    }
}
