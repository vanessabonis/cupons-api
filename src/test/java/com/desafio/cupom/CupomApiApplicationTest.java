package com.desafio.cupom;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest
@ActiveProfiles("test")
@DisplayName("CupomApiApplication - Testes")
class CupomApiApplicationTest {

    @Test
    @DisplayName("Deve carregar contexto Spring com sucesso")
    void deveCarregarContextoSpring() {
        // Spring Boot carrega o contexto automaticamente
        // Se chegar aqui, o teste passou
    }
}

