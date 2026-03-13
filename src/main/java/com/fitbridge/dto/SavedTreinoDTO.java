package com.fitbridge.dto;

import java.util.Set;

/**
 * DTO para representar um treino salvo localmente pelo aluno.
 * Contém uma cópia dos dados do treino e pode ser editado localmente.
 */
public class SavedTreinoDTO {
    private Long id;
    private String titulo;
    private String grupoMuscular;
    private String descricao;
    private Set<SavedTreinoExercicioDTO> itens;

    public SavedTreinoDTO() {}

    public SavedTreinoDTO(Long id, String titulo, String grupoMuscular, String descricao, Set<SavedTreinoExercicioDTO> itens) {
        this.id = id;
        this.titulo = titulo;
        this.grupoMuscular = grupoMuscular;
        this.descricao = descricao;
        this.itens = itens;
    }

    // Getters and Setters
    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitulo() {
        return titulo;
    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getGrupoMuscular() {
        return grupoMuscular;
    }

    public void setGrupoMuscular(String grupoMuscular) {
        this.grupoMuscular = grupoMuscular;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Set<SavedTreinoExercicioDTO> getItens() {
        return itens;
    }

    public void setItens(Set<SavedTreinoExercicioDTO> itens) {
        this.itens = itens;
    }
}
