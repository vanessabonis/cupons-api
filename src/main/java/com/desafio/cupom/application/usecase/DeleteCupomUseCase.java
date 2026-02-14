package com.desafio.cupom.application.usecase;

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
public class DeleteCupomUseCase {

    private final CupomRepository cupomRepository;

    @Transactional
    public void execute(UUID id) {
        log.debug("Deletando cupom com ID: {}", id);

        Cupom cupom = cupomRepository.findById(id)
            .orElseThrow(() -> new CupomNotFoundException(id));

        cupom.delete();
        cupomRepository.save(cupom);

        log.info("Cupom deletado com sucesso. ID: {}", id);
    }
}

