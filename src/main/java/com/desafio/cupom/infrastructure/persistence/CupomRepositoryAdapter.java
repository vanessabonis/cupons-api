package com.desafio.cupom.infrastructure.persistence;

import com.desafio.cupom.domain.entity.Cupom;
import com.desafio.cupom.domain.repository.CupomRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Optional;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class CupomRepositoryAdapter implements CupomRepository {

    private final CupomJpaRepository jpaRepository;

    @Override
    public Cupom save(Cupom cupom) {
        return jpaRepository.save(cupom);
    }

    @Override
    public Optional<Cupom> findById(UUID id) {
        return jpaRepository.findById(id);
    }

    @Override
    public Optional<Cupom> findByCode(String code) {
        return jpaRepository.findByCode(code);
    }

    @Override
    public boolean existsByCode(String code) {
        return jpaRepository.existsByCode(code);
    }
}

