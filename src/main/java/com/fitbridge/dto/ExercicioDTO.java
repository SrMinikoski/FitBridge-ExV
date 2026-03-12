package com.fitbridge.dto;

public class ExercicioDTO {
    private Long id;
    private String descricao;
    private String musculoAlvo;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getMusculoAlvo() { return musculoAlvo; }
    public void setMusculoAlvo(String musculoAlvo) { this.musculoAlvo = musculoAlvo; }
}
