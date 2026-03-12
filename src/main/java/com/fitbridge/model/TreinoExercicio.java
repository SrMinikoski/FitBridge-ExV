package com.fitbridge.model;

import jakarta.persistence.*;
import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class TreinoExercicio {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "treino_id")
    @JsonIgnore
    private Treino treino;

    @ManyToOne(optional = false)
    @JoinColumn(name = "exercicio_id")
    private Exercicio exercicio;

    private Integer series;
    private Integer repeticoes;

    public TreinoExercicio() {
    }

    public TreinoExercicio(Treino treino, Exercicio exercicio, Integer series, Integer repeticoes) {
        this.treino = treino;
        this.exercicio = exercicio;
        this.series = series;
        this.repeticoes = repeticoes;
    }

    public Long getId() {
        return id;
    }

    public Treino getTreino() {
        return treino;
    }

    public void setTreino(Treino treino) {
        this.treino = treino;
    }

    public Exercicio getExercicio() {
        return exercicio;
    }

    public void setExercicio(Exercicio exercicio) {
        this.exercicio = exercicio;
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