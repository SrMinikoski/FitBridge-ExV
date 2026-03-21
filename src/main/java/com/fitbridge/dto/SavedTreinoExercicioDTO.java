package com.fitbridge.dto;

/**
 * DTO para representar um exercício dentro de um treino salvo localmente.
 * Inclui informações completas do exercício para visualização e edição offline.
 */
public class SavedTreinoExercicioDTO {
    private Long id;
    private Long exercicioId;
    private String nome;
    private String descricao;
    private String musculoAlvo;
    private String musculosAuxiliares;
    private String diretorioImagem;
    private Integer series;
    private Integer repeticoes;

    public SavedTreinoExercicioDTO() {}

    public SavedTreinoExercicioDTO(Long id, Long exercicioId, String nome, String descricao, String musculoAlvo, String musculosAuxiliares, String diretorioImagem, Integer series, Integer repeticoes) {
        this.id = id;
        this.exercicioId = exercicioId;
        this.nome = nome;
        this.descricao = descricao;
        this.musculoAlvo = musculoAlvo;
        this.musculosAuxiliares = musculosAuxiliares;
        this.diretorioImagem = diretorioImagem;
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

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMusculosAuxiliares() {
        return musculosAuxiliares;
    }

    public void setMusculosAuxiliares(String musculosAuxiliares) {
        this.musculosAuxiliares = musculosAuxiliares;
    }

    public String getDiretorioImagem() {
        return diretorioImagem;
    }

    public void setDiretorioImagem(String diretorioImagem) {
        this.diretorioImagem = diretorioImagem;
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
