package com.fitbridge.dto;

/**
 * DTO para representar um exercício dentro de um treino salvo localmente.
 * Inclui informações completas do exercício para visualização e edição offline.
 */
public class SavedTreinoExercicioDTO {
    private Long id;  // ID do TreinoExercicio (para referência na edição)
    private Long exercicioId;  // ID do Exercicio original
    private String descricao;  // Descrição do exercício
    private String musculoAlvo;  // Músculo alvo do exercício
    private Integer series;  // Número de séries (editável)
    private Integer repeticoes;  // Número de repetições (editável)

    public SavedTreinoExercicioDTO() {}

    public SavedTreinoExercicioDTO(Long id, Long exercicioId, String descricao, String musculoAlvo, Integer series, Integer repeticoes) {
        this.id = id;
        this.exercicioId = exercicioId;
        this.descricao = descricao;
        this.musculoAlvo = musculoAlvo;
        this.series = series;
        this.repeticoes = repeticoes;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getExercicioId() {
        return exercicioId;
    }

    public void setExercicioId(Long exercicioId) {
        this.exercicioId = exercicioId;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public String getMusculoAlvo() {
        return musculoAlvo;
    }

    public void setMusculoAlvo(String musculoAlvo) {
        this.musculoAlvo = musculoAlvo;
    }

    public Integer getSeries() {
        return series;
    }

    public void setSeries(Integer series) {
        this.series = series;
    }

    public Integer getRepeticoes() {
        return repeticoes;
    }

    public void setRepeticoes(Integer repeticoes) {
        this.repeticoes = repeticoes;
    }
}
