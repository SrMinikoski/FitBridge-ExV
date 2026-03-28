package com.fitbridge.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.fitbridge.model.Treino;
import java.util.List;


public interface TreinoRepository extends JpaRepository<Treino, Long> {

    // Função 3: listar todos os treinos criados por um instrutor
    List<Treino> findByInstrutorId(Long instrutorId);


}