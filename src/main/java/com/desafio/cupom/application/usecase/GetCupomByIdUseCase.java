package com.desafio.cupom.application.usecase;

import com.desafio.cupom.application.dto.CupomResponse;
import com.desafio.cupom.application.mapper.CupomMapper;
import com.desafio.cupom.domain.entity.Cupom;
import com.desafio.cupom.domain.exception.CupomNotFoundException;
import com.desafio.cupom.domain.repository.CupomRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
@Slf4j
public class GetCupomByIdUseCase {

    private final CupomRepository cupomRepository;
    private final CupomMapper cupomMapper;

    @Transactional(readOnly = true)
    public CupomResponse execute(UUID id) {
        log.debug("Buscando cupom por ID: {}", id);

        Cupom cupom = cupomRepository.findById(id)
            .orElseThrow(() -> new CupomNotFoundException(id));

        log.debug("Cupom encontrado. ID: {}, Code: {}", cupom.getId(), cupom.getCodeValue());

        return cupomMapper.toResponse(cupom);
    }
}

