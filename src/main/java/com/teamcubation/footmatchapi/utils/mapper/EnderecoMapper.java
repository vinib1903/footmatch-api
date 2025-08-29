package com.teamcubation.footmatchapi.utils.mapper;

import com.teamcubation.footmatchapi.domain.entities.Endereco;
import com.teamcubation.footmatchapi.application.dto.response.EnderecoResponseDTO;
import com.teamcubation.footmatchapi.application.dto.response.ViaCepResponseDTO;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")

public interface EnderecoMapper {

    Endereco fromViaCepResponse(ViaCepResponseDTO response);
    EnderecoResponseDTO toDto(Endereco endereco);
}
