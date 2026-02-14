package com.desafio.cupom.application.mapper;

import com.desafio.cupom.application.dto.CupomResponse;
import com.desafio.cupom.domain.entity.Cupom;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface CupomMapper {

    @Mapping(source = "codeValue", target = "code")
    @Mapping(source = "discountValueAmount", target = "discountValue")
    CupomResponse toResponse(Cupom cupom);
}

