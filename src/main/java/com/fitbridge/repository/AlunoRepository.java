package com.fitbridge.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.fitbridge.model.Aluno;


import java.util.Optional;


public interface AlunoRepository extends JpaRepository<Aluno, Long> {
    Optional<Aluno> findByEmail(String email);
}