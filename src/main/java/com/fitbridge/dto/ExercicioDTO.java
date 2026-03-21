package com.fitbridge.dto;

public class ExercicioDTO {
    private Long id;
    private String nome;
    private String descricao;
    private String musculoAlvo;
    private String musculosAuxiliares;
    private String diretorioImagem;

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
