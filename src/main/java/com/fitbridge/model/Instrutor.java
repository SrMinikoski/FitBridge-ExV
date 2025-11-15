package com.fitbridge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "instrutor")
public class Instrutor {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String nome;
    private String sexo;
    private Integer idade;
    private String crefDiploma;
    private String email;
    private String senha;


    @OneToMany(mappedBy = "instrutor", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Treino> treinos = new ArrayList<>();


    public Instrutor() {}


    public Instrutor(String nome, String sexo, Integer idade, String crefDiploma, String email, String senha) {
        this.nome = nome;
        this.sexo = sexo;
        this.idade = idade;
        this.crefDiploma = crefDiploma;
        this.email = email;
        this.senha = senha;
    }


    // getters/setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }
    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }
    public String getSexo() { return sexo; }
    public void setSexo(String sexo) { this.sexo = sexo; }
    public Integer getIdade() { return idade; }
    public void setIdade(Integer idade) { this.idade = idade; }
    public String getCrefDiploma() { return crefDiploma; }
    public void setCrefDiploma(String crefDiploma) { this.crefDiploma = crefDiploma; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public List<Treino> getTreinos() { return treinos; }
    public void setTreinos(List<Treino> treinos) { this.treinos = treinos; }
}