package com.desafio.cupom.api.controller;

import com.desafio.cupom.application.dto.CupomRequest;
import com.desafio.cupom.application.dto.CupomResponse;
import com.desafio.cupom.application.usecase.CreateCupomUseCase;
import com.desafio.cupom.application.usecase.DeleteCupomUseCase;
import com.desafio.cupom.application.usecase.GetCupomByIdUseCase;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/coupon")
@RequiredArgsConstructor
@Slf4j
@Tag(name = "Cupons", description = "API para gerenciamento de cupons de desconto")
public class CupomController {

    private final CreateCupomUseCase createCupomUseCase;
    private final GetCupomByIdUseCase getCupomByIdUseCase;
    private final DeleteCupomUseCase deleteCupomUseCase;

    @PostMapping
    @Operation(summary = "Criar novo cupom", description = "Cria um novo cupom de desconto")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Cupom criado com sucesso"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<CupomResponse> create(@Valid @RequestBody CupomRequest request) {
        log.info("POST /coupon - Criando cupom com código: {}", request.code());

        CupomResponse response = createCupomUseCase.execute(request);

        log.info("Cupom criado com sucesso. ID: {}", response.id());
        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @GetMapping("/{id}")
    @Operation(summary = "Buscar cupom por ID", description = "Retorna um cupom específico pelo ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Cupom encontrado"),
        @ApiResponse(responseCode = "404", description = "Cupom não encontrado")
    })
    public ResponseEntity<CupomResponse> findById(@PathVariable UUID id) {
        log.info("GET /coupon/{} - Buscando cupom", id);

        CupomResponse response = getCupomByIdUseCase.execute(id);

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar cupom", description = "Realiza soft delete de um cupom")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "204", description = "Cupom deletado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Cupom não encontrado"),
        @ApiResponse(responseCode = "409", description = "Cupom já foi deletado")
    })
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        log.info("DELETE /coupon/{} - Deletando cupom", id);

        deleteCupomUseCase.execute(id);

        log.info("Cupom {} deletado com sucesso", id);
        return ResponseEntity.noContent().build();
    }
}
