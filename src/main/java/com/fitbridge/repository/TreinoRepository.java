package com.fitbridge.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.fitbridge.model.Treino;


public interface TreinoRepository extends JpaRepository<Treino, Long> {}