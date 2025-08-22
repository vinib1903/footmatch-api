package com.teamcubation.footmatchapi.domain.entities;

import com.teamcubation.footmatchapi.domain.exceptions.EntidadeEmUsoException;
import com.teamcubation.footmatchapi.domain.exceptions.RegraDeNegocioException;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class Partida {
    private Long id;
    private Clube mandante;
    private Clube visitante;
    private Estadio estadio;
    private LocalDateTime dataHora;
    private Integer golsMandante;
    private Integer golsVisitante;

    private Partida(Clube mandante, Clube visitante, Estadio estadio, LocalDateTime dataHora, Integer golsMandante, Integer golsVisitante) {
        this.mandante = mandante;
        this.visitante = visitante;
        this.estadio = estadio;
        this.dataHora = dataHora;
        this.golsMandante = golsMandante;
        this.golsVisitante = golsVisitante;
    }

    public static Partida criar(Clube mandante, Clube visitante, Estadio estadio, LocalDateTime dataHora, Integer golsMandante, Integer golsVisitante) {
        validarClubesAtivos(mandante, visitante);
        validarClubesDiferentes(mandante, visitante);
        validarDataPartidaAnteriorCriacaoClube(mandante, visitante, dataHora.toLocalDate());
        validarDataCriacaoFutura(dataHora);
        validarGolsNegativos(golsMandante, golsVisitante);

        return new Partida(mandante, visitante, estadio, dataHora, golsMandante, golsVisitante);
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

    private static void validarDataPartidaAnteriorCriacaoClube(Clube mandante, Clube visitante, LocalDate dataPartida) {
        if (dataPartida.isBefore(mandante.getDataCriacao()) || dataPartida.isBefore(visitante.getDataCriacao())) {
            throw new EntidadeEmUsoException("A data da partida não pode ser anterior à data de criação dos clubes.");
        }
    }

    private static void validarDataCriacaoFutura(LocalDateTime dataHora) {
        if (dataHora.isAfter(LocalDateTime.now())) {
            throw new RegraDeNegocioException("A data da partida não pode ser no futuro.");
        }
    }

    private static void validarGolsNegativos(Integer golsMandante, Integer golsVisitante) {
        if (golsMandante < 0 || golsVisitante < 0) {
            throw new RegraDeNegocioException("Gols não podem ser negativos.");
        }
    }

    private static void validarClubesAtivos(Clube mandante, Clube visitante) {
        if (!mandante.getAtivo() || !visitante.getAtivo()) {
            throw new EntidadeEmUsoException("Não é possível criar partida com clubes inativos.");
        }
    }

    private static void validarClubesDiferentes(Clube mandante, Clube visitante) {
        if (mandante.equals(visitante)) {
            throw new RegraDeNegocioException("O clube mandante não pode ser o mesmo que o visitante.");
        }
    }
}
