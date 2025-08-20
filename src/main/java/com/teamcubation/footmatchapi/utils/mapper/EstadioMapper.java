package com.teamcubation.footmatchapi.utils.mapper;

import com.teamcubation.footmatchapi.domain.entities.Estadio;
import com.teamcubation.footmatchapi.application.dto.request.EstadioRequestDTO;
import com.teamcubation.footmatchapi.application.dto.response.EstadioResponseDTO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses =  {EnderecoMapper.class})
public interface EstadioMapper {
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "endereco", ignore = true)
    Estadio toEntity(EstadioRequestDTO estadioRequestDTO);
    EstadioResponseDTO toDto(Estadio estadio);
}
