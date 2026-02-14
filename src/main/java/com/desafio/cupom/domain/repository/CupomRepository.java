package com.desafio.cupom.domain.repository;

import com.desafio.cupom.domain.entity.Cupom;

import java.util.Optional;
import java.util.UUID;

public interface CupomRepository {

    Cupom save(Cupom cupom);

    Optional<Cupom> findById(UUID id);

    Optional<Cupom> findByCode(String code);

    boolean existsByCode(String code);
}

