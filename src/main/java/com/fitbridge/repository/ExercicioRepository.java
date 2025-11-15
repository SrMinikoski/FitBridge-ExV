package com.fitbridge.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.fitbridge.model.Exercicio;


public interface ExercicioRepository extends JpaRepository<Exercicio, Long> {}