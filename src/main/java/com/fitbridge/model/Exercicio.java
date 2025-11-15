package com.fitbridge.model;


import jakarta.persistence.*;


@Entity
public class Exercicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String descricao;
    private String musculoAlvo;
    private Integer series;
    private Integer repeticoes;


    public Exercicio() {}


    public Exercicio(String descricao, String musculoAlvo, Integer series, Integer repeticoes) {
        this.descricao = descricao;
        this.musculoAlvo = musculoAlvo;
        this.series = series;
        this.repeticoes = repeticoes;
    }


    // getters and setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getMusculoAlvo() { return musculoAlvo; }
    public void setMusculoAlvo(String musculoAlvo) { this.musculoAlvo = musculoAlvo; }
    public Integer getSeries() { return series; }
    public void setSeries(Integer series) { this.series = series; }
    public Integer getRepeticoes() { return repeticoes; }
    public void setRepeticoes(Integer repeticoes) { this.repeticoes = repeticoes; }
}