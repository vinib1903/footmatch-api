package com.teamcubation.footmatchapi.domain.entities;

import com.teamcubation.footmatchapi.domain.enums.SiglaEstado;
import java.time.LocalDate;

public class Clube {
    private Long id;
    private String nome;
    private SiglaEstado siglaEstado;
    private Boolean ativo;
    private LocalDate dataCriacao;

    public Clube() {}

    public Clube(Long id, String nome, SiglaEstado siglaEstado, Boolean ativo, LocalDate dataCriacao) {
        this.id = id;
        this.nome = nome;
        this.siglaEstado = siglaEstado;
        this.ativo = ativo;
        this.dataCriacao = dataCriacao;
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
}
