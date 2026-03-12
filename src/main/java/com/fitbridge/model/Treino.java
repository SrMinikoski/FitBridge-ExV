package com.fitbridge.model;


import jakarta.persistence.*;

import java.util.HashSet;
import java.util.Set;

@Entity
public class Treino {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String titulo;
    private String grupoMuscular;
    @Column(length = 2000)
    private String descricao;


    @OneToMany(mappedBy = "treino", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<TreinoExercicio> itens = new HashSet<>();


    @ManyToOne
    @JoinColumn(name = "instrutor_id")
    private Instrutor instrutor;


    public Treino() {}


    public Treino(String titulo, String grupoMuscular, String descricao) {
        this.titulo = titulo;
        this.grupoMuscular = grupoMuscular;
        this.descricao = descricao;
    }


    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getTitulo() { return titulo; }
    public void setTitulo(String titulo) { this.titulo = titulo; }
    public String getGrupoMuscular() { return grupoMuscular; }
    public void setGrupoMuscular(String grupoMuscular) { this.grupoMuscular = grupoMuscular; }
    public String getDescricao() { return descricao; }
    public void setDescricao(String descricao) { this.descricao = descricao; }
    public Set<TreinoExercicio> getItens() { return itens; }
    public void setItens(Set<TreinoExercicio> itens) { this.itens = itens; }
    public Instrutor getInstrutor() { return instrutor; }
    public void setInstrutor(Instrutor instrutor) { this.instrutor = instrutor; }
}