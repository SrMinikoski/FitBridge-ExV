package com.fitbridge.dto;

import java.util.Set;
import com.fitbridge.dto.TreinoExercicioDTO;

public class TreinoDTO {
    private Long id;
    private String titulo;
    private String grupoMuscular;
    private String descricao;
    private String diretorioImagem;
    private Set<TreinoExercicioDTO> itens;
    private Long instrutorId;

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getGrupoMuscular() { return grupoMuscular; }
    public void setGrupoMuscular(String grupoMuscular) { this.grupoMuscular = grupoMuscular; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public String getDiretorioImagem() { return diretorioImagem; }
    public void setDiretorioImagem(String diretorioImagem) { this.diretorioImagem = diretorioImagem; }
    public Set<TreinoExercicioDTO> getItens() { return itens; }
    public void setItens(Set<TreinoExercicioDTO> itens) { this.itens = itens; }
    public Long getInstrutorId() { return instrutorId; }
    public void setInstrutorId(Long instrutorId) { this.instrutorId = instrutorId; }
}
