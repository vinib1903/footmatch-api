package com.teamcubation.footmatchapi.application.service.estadio;

import com.teamcubation.footmatchapi.application.ports.out.EstadioEventsPort;
import com.teamcubation.footmatchapi.application.ports.out.ViacepClientPort;
import com.teamcubation.footmatchapi.application.usecase.EstadioUseCases;
import com.teamcubation.footmatchapi.domain.entities.Endereco;
import com.teamcubation.footmatchapi.domain.entities.Estadio;
import com.teamcubation.footmatchapi.application.dto.request.EstadioRequestDTO;
import com.teamcubation.footmatchapi.application.dto.response.EstadioResponseDTO;
import com.teamcubation.footmatchapi.application.dto.response.ViaCepResponseDTO;
import com.teamcubation.footmatchapi.application.ports.out.EstadioRepository;
import com.teamcubation.footmatchapi.domain.exceptions.EntidadeEmUsoException;
import com.teamcubation.footmatchapi.domain.exceptions.EntidadeNaoEncontradaException;
import com.teamcubation.footmatchapi.utils.mapper.EnderecoMapper;
import com.teamcubation.footmatchapi.utils.mapper.EstadioMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import java.util.Optional;

@Service
public class EstadioServiceImpl implements EstadioUseCases {

    public final EstadioRepository estadioRepository;
    private final EstadioMapper estadioMapper;
    private final EnderecoMapper enderecoMapper;
    private final ViacepClientPort viacepClientPort;
    private final EstadioEventsPort estadioEventsPort;

    public EstadioServiceImpl(EstadioRepository estadioRepository, EstadioMapper estadioMapper, EnderecoMapper enderecoMapper, ViacepClientPort viacepClientPort, EstadioEventsPort estadioEventsPort) {
        this.estadioRepository = estadioRepository;
        this.estadioMapper = estadioMapper;
        this.enderecoMapper = enderecoMapper;
        this.viacepClientPort = viacepClientPort;
        this.estadioEventsPort = estadioEventsPort;
    }

    public EstadioResponseDTO criarEstadio(EstadioRequestDTO estadioRequestDTO) {

        validarNomeExistente(estadioRequestDTO.getNome(), null);

        validarCepExistente(estadioRequestDTO.getCep(), null);

        ViaCepResponseDTO viaCepResponse = viacepClientPort.buscarEnderecoPorCep(estadioRequestDTO.getCep());

        Endereco endereco = enderecoMapper.fromViaCepResponse(viaCepResponse);

        endereco.setCep(normalizarCep(viaCepResponse.getCep()));

        Estadio estadio = Estadio.criar(estadioRequestDTO.getNome(), endereco);

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
                .orElseThrow(() -> new EntidadeNaoEncontradaException("Estádio com id " + id + " não encontrado."));
    }

    @Override
    public void solicitarCriacaoEstadio(EstadioRequestDTO dto) {
        estadioEventsPort.notificarCriacaoEstadio(dto);
    }

    @Override
    public void solicitarAtualizacaoEstadio(Long id, EstadioRequestDTO dto) {
        estadioEventsPort.notificarAtualizacaoEstadio(id, dto);
    }

    private String normalizarCep(String cep) {

        return cep.replaceAll("\\D", "");
    }

    private void validarNomeExistente(String nome, Long id) {

        Optional<Estadio> estadioExistente = estadioRepository.findByNome(nome);

        if (estadioExistente.isPresent() && (id == null || !estadioExistente.get().getId().equals(id))) {
            throw new EntidadeEmUsoException("Já existe um estádio com este nome.");
        }
    }

    private void validarCepExistente(String cep, Long id) {

        Optional<Estadio> estadioExistente = estadioRepository.findByEndereco_Cep(cep);

        if (estadioExistente.isPresent()
                && (id == null || !estadioExistente.get().getId().equals(id))) {
            throw new EntidadeEmUsoException("Já existe um estádio cadastrado neste CEP.");
        }
    }

}
