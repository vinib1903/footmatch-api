package com.teamcubation.footmatchapi.utils.mapper;

import com.teamcubation.footmatchapi.adapters.outbound.entities.ClubeJpaEntity;
import com.teamcubation.footmatchapi.domain.entities.Clube;
import com.teamcubation.footmatchapi.application.dto.request.ClubeRequestDTO;
import com.teamcubation.footmatchapi.application.dto.response.ClubeResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ClubeMapper {
    @Mapping(target = "id", ignore = true)
    Clube dtoToEntity(ClubeRequestDTO clubeRequestDTO);
    ClubeResponseDTO entityToDto(Clube clube);
    Clube jpaToEntity(ClubeJpaEntity clubeJpaEntity);


}
