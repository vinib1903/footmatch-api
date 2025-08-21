package com.teamcubation.footmatchapi.domain.entities;

import java.time.LocalDateTime;

public class Partida {
    private Long id;
    private Clube mandante;
    private Clube visitante;
    private Estadio estadio;
    private LocalDateTime dataHora;
    private Integer golsMandante;
    private Integer golsVisitante;

    public Partida() {}

    public Partida(Long id, Clube mandante, Clube visitante, Estadio estadio, LocalDateTime dataHora, Integer golsMandante, Integer golsVisitante) {
        this.id = id;
        this.mandante = mandante;
        this.visitante = visitante;
        this.estadio = estadio;
        this.dataHora = dataHora;
        this.golsMandante = golsMandante;
        this.golsVisitante = golsVisitante;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Clube getMandante() {
        return mandante;
    }

    public void setMandante(Clube mandante) {
        this.mandante = mandante;
    }

    public Clube getVisitante() {
        return visitante;
    }

    public void setVisitante(Clube visitante) {
        this.visitante = visitante;
    }

    public Estadio getEstadio() {
        return estadio;
    }

    public void setEstadio(Estadio estadio) {
        this.estadio = estadio;
    }

    public LocalDateTime getDataHora() {
        return dataHora;
    }

    public void setDataHora(LocalDateTime dataHora) {
        this.dataHora = dataHora;
    }

    public Integer getGolsMandante() {
        return golsMandante;
    }

    public void setGolsMandante(Integer golsMandante) {
        this.golsMandante = golsMandante;
    }

    public Integer getGolsVisitante() {
        return golsVisitante;
    }

    public void setGolsVisitante(Integer golsVisitante) {
        this.golsVisitante = golsVisitante;
    }
}
