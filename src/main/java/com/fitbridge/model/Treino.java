package com.fitbridge.model;


import jakarta.persistence.*;

import java.text.Normalizer;
import java.util.HashSet;
import java.util.Set;

@Entity
public class Treino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String titulo;
    private String tituloNormalizado;
    private String grupoMuscular;
    private String grupoMuscularNormalizado;
    @Column(length = 2000)
    private String descricao;
    private String diretorioImagem;


    @OneToMany(mappedBy = "treino", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TreinoExercicio> itens = new HashSet<>();


    @ManyToOne
    @JoinColumn(name = "instrutor_id")
    private Instrutor instrutor;


    public Treino() {}


    public Treino(String titulo, String grupoMuscular, String descricao) {
        setTitulo(titulo);
        setGrupoMuscular(grupoMuscular);
        this.descricao = descricao;
    }

    private static String normalizar(String valor) {
        if (valor == null) return null;
        return Normalizer.normalize(valor, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}", "")
                .toLowerCase();
    }

    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; this.tituloNormalizado = normalizar(titulo); }
    public String getTituloNormalizado() { return tituloNormalizado; }
    public String getGrupoMuscular() { return grupoMuscular; }
    public void setGrupoMuscular(String grupoMuscular) { this.grupoMuscular = grupoMuscular; this.grupoMuscularNormalizado = normalizar(grupoMuscular); }
    public String getGrupoMuscularNormalizado() { return grupoMuscularNormalizado; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Set<TreinoExercicio> getItens() { return itens; }
    public void setItens(Set<TreinoExercicio> itens) { this.itens = itens; }
    public Instrutor getInstrutor() { return instrutor; }
    public void setInstrutor(Instrutor instrutor) { this.instrutor = instrutor; }
    public String getDiretorioImagem() { return diretorioImagem; }
    public void setDiretorioImagem(String diretorioImagem) { this.diretorioImagem = diretorioImagem; }
}