package com.fitbridge.dto;

import java.time.LocalDateTime;

/**
 * DTO para representar um treino favorito do aluno com seus dados locais editáveis.
 */
public class FavoritoDTO {
    private Long id;
    private Long alunoId;
    private LocalDateTime dataInclusao;
    private SavedTreinoDTO treinoDados;  // Cópia dos dados do treino salvos localmente

    public FavoritoDTO() {}

    public FavoritoDTO(Long id, Long alunoId, LocalDateTime dataInclusao, SavedTreinoDTO treinoDados) {
        this.id = id;
        this.alunoId = alunoId;
        this.dataInclusao = dataInclusao;
        this.treinoDados = treinoDados;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getAlunoId() {
        return alunoId;
    }

    public void setAlunoId(Long alunoId) {
        this.alunoId = alunoId;
    }

    public LocalDateTime getDataInclusao() {
        return dataInclusao;
    }

    public void setDataInclusao(LocalDateTime dataInclusao) {
        this.dataInclusao = dataInclusao;
    }

    public SavedTreinoDTO getTreinoDados() {
        return treinoDados;
    }

    public void setTreinoDados(SavedTreinoDTO treinoDados) {
        this.treinoDados = treinoDados;
    }
}

