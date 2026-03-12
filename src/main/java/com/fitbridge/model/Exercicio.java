package com.fitbridge.model;


import jakarta.persistence.*;


@Entity
public class Exercicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String descricao;
    private String musculoAlvo;

    public Exercicio() {}

    public Exercicio(String descricao, String musculoAlvo) {
        this.descricao = descricao;
        this.musculoAlvo = musculoAlvo;
    }


    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getMusculoAlvo() { return musculoAlvo; }
    public void setMusculoAlvo(String musculoAlvo) { this.musculoAlvo = musculoAlvo; }
}