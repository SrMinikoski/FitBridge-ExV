package com.fitbridge.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "aluno")
public class Aluno {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    private String nome;
    private String sexo;
    private Integer idade;
    private Float altura;
    private Float peso;
    private String objetivo;
    private String email;
    private String senha;


    @OneToMany(mappedBy = "aluno", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Favorito> favoritos = new ArrayList<>();


    public Aluno() {}


    public Aluno(String nome, String sexo, Integer idade, Float altura, Float peso, String objetivo, String email, String senha) {
        this.nome = nome;
        this.sexo = sexo;
        this.idade = idade;
        this.altura = altura;
        this.peso = peso;
        this.objetivo = objetivo;
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
    public Float getAltura() { return altura; }
    public void setAltura(Float altura) { this.altura = altura; }
    public Float getPeso() { return peso; }
    public void setPeso(Float peso) { this.peso = peso; }
    public String getObjetivo() { return objetivo; }
    public void setObjetivo(String objetivo) { this.objetivo = objetivo; }
    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }
    public String getSenha() { return senha; }
    public void setSenha(String senha) { this.senha = senha; }
    public List<Favorito> getFavoritos() { return favoritos; }
    public void setFavoritos(List<Favorito> favoritos) { this.favoritos = favoritos; }
}