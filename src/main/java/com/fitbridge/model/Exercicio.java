package com.fitbridge.model;


import jakarta.persistence.*;


@Entity
public class Exercicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    @Column(length = 2000)
    private String descricao;
    private String musculoAlvo;
    private String musculosAuxiliares;
    private String diretorioImagem;

    public Exercicio() {}

    public Exercicio(String nome, String descricao, String musculoAlvo, String musculosAuxiliares, String diretorioImagem) {
        this.nome = nome;
        this.descricao = descricao;
        this.musculoAlvo = musculoAlvo;
        this.musculosAuxiliares = musculosAuxiliares;
        this.diretorioImagem = diretorioImagem;
    }


    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getMusculoAlvo() { return musculoAlvo; }
    public void setMusculoAlvo(String musculoAlvo) { this.musculoAlvo = musculoAlvo; }
    public String getMusculosAuxiliares() { return musculosAuxiliares; }
    public void setMusculosAuxiliares(String musculosAuxiliares) { this.musculosAuxiliares = musculosAuxiliares; }
    public String getDiretorioImagem() { return diretorioImagem; }
    public void setDiretorioImagem(String diretorioImagem) { this.diretorioImagem = diretorioImagem; }
}