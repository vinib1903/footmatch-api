package com.teamcubation.footmatchapi.domain.entities;

import com.teamcubation.footmatchapi.domain.enums.SiglaEstado;
import com.teamcubation.footmatchapi.domain.exceptions.RegraDeNegocioException;

import java.time.LocalDate;

public class Clube {
    private Long id;
    private String nome;
    private SiglaEstado siglaEstado;
    private Boolean ativo;
    private LocalDate dataCriacao;

    public Clube() {}

    private Clube(String nome, SiglaEstado siglaEstado, Boolean ativo, LocalDate dataCriacao) {
        this.nome = nome;
        this.siglaEstado = siglaEstado;
        this.ativo = ativo;
        this.dataCriacao = dataCriacao;
    }

    public static Clube criar(String nome, String siglaEstadoStr, Boolean ativo, LocalDate dataCriacao) {
        validarDataCriacaoFutura(dataCriacao);
        SiglaEstado sigla = validarSiglaEstado(siglaEstadoStr);

        return new Clube(nome, sigla, ativo, dataCriacao);
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public SiglaEstado getSiglaEstado() {
        return siglaEstado;
    }

    public void setSiglaEstado(SiglaEstado siglaEstado) {
        this.siglaEstado = siglaEstado;
    }

    public Boolean getAtivo() {
        return ativo;
    }

    public void setAtivo(Boolean ativo) {
        this.ativo = ativo;
    }

    public LocalDate getDataCriacao() {
        return dataCriacao;
    }

    public void setDataCriacao(LocalDate dataCriacao) {
        this.dataCriacao = dataCriacao;
    }

    private static void validarDataCriacaoFutura(LocalDate dataCriacao) {
        if (dataCriacao.isAfter(LocalDate.now())) {
            throw new RegraDeNegocioException("Data de criação não pode ser no futuro.");
        }
    }

    private static SiglaEstado validarSiglaEstado(String siglaEstadoStr) {
        try {
            return SiglaEstado.valueOf(siglaEstadoStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            throw new RegraDeNegocioException("Sigla do estado inválida: " + siglaEstadoStr);
        }
    }
}
