package com.fitbridge.model;


import jakarta.persistence.*;
import java.time.LocalDateTime;


@Entity
public class Favorito {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private LocalDateTime dataInclusao = LocalDateTime.now();


    @ManyToOne
    private Aluno aluno;


    @ManyToOne
    private Treino treino;


    public Favorito() {}


    public Favorito(Aluno aluno, Treino treino) {
        this.aluno = aluno;
        this.treino = treino;
    }


    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public LocalDateTime getDataInclusao() { return dataInclusao; }
    public void setDataInclusao(LocalDateTime dataInclusao) { this.dataInclusao = dataInclusao; }
    public Aluno getAluno() { return aluno; }
    public void setAluno(Aluno aluno) { this.aluno = aluno; }
    public Treino getTreino() { return treino; }
    public void setTreino(Treino treino) { this.treino = treino; }
}