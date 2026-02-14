package com.desafio.cupom.infrastructure.persistence;

import com.desafio.cupom.domain.entity.Cupom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;
import java.util.UUID;

@Repository
public interface CupomJpaRepository extends JpaRepository<Cupom, UUID> {

    @Query("SELECT c FROM Cupom c WHERE c.code.value = :code")
    Optional<Cupom> findByCode(@Param("code") String code);

    @Query("SELECT CASE WHEN COUNT(c) > 0 THEN true ELSE false END FROM Cupom c WHERE c.code.value = :code")
    boolean existsByCode(@Param("code") String code);
}

