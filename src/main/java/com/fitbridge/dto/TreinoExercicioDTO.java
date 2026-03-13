package com.fitbridge.dto;

public class TreinoExercicioDTO {
    private Long id;  // ID do TreinoExercicio
    private Long exercicioId;
    private String descricaoExercicio;  // Descrição do exercício
    private String musculoAlvo;  // Músculo alvo
    private Integer series;
    private Integer repeticoes;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public Long getExercicioId() { return exercicioId; }
    public void setExercicioId(Long exercicioId) { this.exercicioId = exercicioId; }
    public String getDescricaoExercicio() { return descricaoExercicio; }
    public void setDescricaoExercicio(String descricaoExercicio) { this.descricaoExercicio = descricaoExercicio; }
    public String getMusculoAlvo() { return musculoAlvo; }
    public void setMusculoAlvo(String musculoAlvo) { this.musculoAlvo = musculoAlvo; }
    public Integer getSeries() { return series; }
    public void setSeries(Integer series) { this.series = series; }
    public Integer getRepeticoes() { return repeticoes; }
    public void setRepeticoes(Integer repeticoes) { this.repeticoes = repeticoes; }
}