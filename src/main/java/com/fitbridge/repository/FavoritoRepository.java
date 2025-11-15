package com.fitbridge.repository;


import org.springframework.data.jpa.repository.JpaRepository;
import com.fitbridge.model.Favorito;
import java.util.List;


public interface FavoritoRepository extends JpaRepository<Favorito, Long> {
    List<Favorito> findByAlunoId(Long alunoId);
}