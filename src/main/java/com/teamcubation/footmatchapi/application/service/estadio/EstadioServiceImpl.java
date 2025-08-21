package com.teamcubation.footmatchapi.application.service.estadio;

import com.teamcubation.footmatchapi.adapters.outbound.integration.ViacepClientPort;
import com.teamcubation.footmatchapi.application.usecase.EstadioUseCases;
import com.teamcubation.footmatchapi.domain.entities.Endereco;
import com.teamcubation.footmatchapi.domain.entities.Estadio;
import com.teamcubation.footmatchapi.application.dto.request.EstadioRequestDTO;
import com.teamcubation.footmatchapi.application.dto.response.EstadioResponseDTO;
import com.teamcubation.footmatchapi.application.dto.response.ViaCepResponseDTO;
import com.teamcubation.footmatchapi.domain.interfaces.EstadioRepository;
import com.teamcubation.footmatchapi.utils.mapper.EnderecoMapper;
import com.teamcubation.footmatchapi.utils.mapper.EstadioMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;
import java.util.Optional;

@Service
public class EstadioServiceImpl implements EstadioUseCases {

    public final EstadioRepository estadioRepository;
    private final EstadioMapper estadioMapper;
    private final EnderecoMapper enderecoMapper;
    private final ViacepClientPort viacepClientPort;

    public EstadioServiceImpl(EstadioRepository estadioRepository, EstadioMapper estadioMapper, EnderecoMapper enderecoMapper, ViacepClientPort viacepClientPort) {
        this.estadioRepository = estadioRepository;
        this.estadioMapper = estadioMapper;
        this.enderecoMapper = enderecoMapper;
        this.viacepClientPort = viacepClientPort;
    }

    public EstadioResponseDTO criarEstadio(EstadioRequestDTO estadioRequestDTO) {

        validarNomeExistente(estadioRequestDTO.getNome(), null);

        validarCepExistente(estadioRequestDTO.getCep(), null);

        ViaCepResponseDTO viaCepResponse = viacepClientPort.buscarEnderecoPorCep(estadioRequestDTO.getCep());

        Endereco endereco = enderecoMapper.fromViaCepResponse(viaCepResponse);

        endereco.setCep(normalizarCep(viaCepResponse.getCep()));

        Estadio estadio = estadioMapper.dtoToEntity(estadioRequestDTO);

        estadio.setEndereco(endereco);

        estadioRepository.save(estadio);

        return estadioMapper.entityToDto(estadio);
    }

    public Page<EstadioResponseDTO> obterEstadios(String nome, Pageable pageable) {

        return estadioRepository.findStadiumsWichFilters(nome,pageable).map(estadioMapper::entityToDto);
    }

    public EstadioResponseDTO obterEstadioPorId(Long id) {

        Estadio estadio = validarExistenciaEstadio(id);

        return estadioMapper.entityToDto(estadio);
    }

    public EstadioResponseDTO atualizarEstadio(Long id, EstadioRequestDTO estadioRequestDTO) {

        Estadio estadio = validarExistenciaEstadio(id);

        validarNomeExistente(estadioRequestDTO.getNome(), id);

        validarCepExistente(estadioRequestDTO.getCep(), id);

        ViaCepResponseDTO viaCepResponse = viacepClientPort.buscarEnderecoPorCep(estadioRequestDTO.getCep());

        Endereco endereco = enderecoMapper.fromViaCepResponse(viaCepResponse);

        endereco.setCep(normalizarCep(viaCepResponse.getCep()));

        estadio.setEndereco(endereco);

        estadio.setNome(estadioRequestDTO.getNome());

        Estadio salvo = estadioRepository.save(estadio);

        return estadioMapper.entityToDto(salvo);
    }

    public Estadio validarExistenciaEstadio(Long id) {

        return estadioRepository.findById(id)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Estádio nao encontrado."));
    }

    private void validarNomeExistente(String nome, Long id) throws ResponseStatusException {

        Optional<Estadio> estadioExistente = estadioRepository.findByNome(nome);

        if (estadioExistente.isPresent() && !estadioExistente.get().getId().equals(id)) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um estádio com este nome.");
        }
    }

    private String normalizarCep(String cep) {

        return cep.replaceAll("\\D", "");
    }

    private void validarCepExistente(String cep, Long id) {

        String cepNormalizado = normalizarCep(cep);
        Optional<Estadio> estadioExistente = estadioRepository.findByEndereco_Cep(cepNormalizado);

        if (estadioExistente.isPresent()
                && (id == null || !estadioExistente.get().getId().equals(id))) {
            throw new ResponseStatusException(HttpStatus.CONFLICT, "Já existe um estádio cadastrado neste CEP.");
        }
    }

}
