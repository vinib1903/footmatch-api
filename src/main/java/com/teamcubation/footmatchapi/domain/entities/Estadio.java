package com.teamcubation.footmatchapi.domain.entities;

public class Estadio {
    private Long id;
    private String nome;
    private Endereco endereco;

    private Estadio(String nome, Endereco endereco) {
        this.nome = nome;
        this.endereco = endereco;
    }

    public static Estadio criar(String nome, Endereco endereco) {
        // Validações puras (que não dependem de repositório) podem ser adicionadas aqui no futuro.
        return new Estadio(nome, endereco);
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

    public Endereco getEndereco() {
        return endereco;
    }

    public void setEndereco(Endereco endereco) {
        this.endereco = endereco;
    }
}
