package com.desafio.cupom.application.usecase;

import com.desafio.cupom.application.dto.CupomRequest;
import com.desafio.cupom.application.dto.CupomResponse;
import com.desafio.cupom.application.mapper.CupomMapper;
import com.desafio.cupom.domain.entity.Cupom;
import com.desafio.cupom.domain.repository.CupomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CreateCupomUseCase {

    private final CupomRepository cupomRepository;
    private final CupomMapper cupomMapper;

    @Transactional
    public CupomResponse execute(CupomRequest request) {
        log.debug("Criando cupom com codigo: {}", request.code());

        Cupom cupom = Cupom.create(
            request.code(),
            request.description(),
            request.discountValue(),
            request.expirationDate(),
            request.published()
        );

        Cupom saved = cupomRepository.save(cupom);
        log.info("Cupom criado com sucesso. ID: {}, Code: {}", saved.getId(), saved.getCodeValue());

        return cupomMapper.toResponse(saved);
    }
}

