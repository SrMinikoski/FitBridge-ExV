package com.fitbridge.controller;
import java.util.List;
import java.util.Optional;


import com.fitbridge.model.*;
import com.fitbridge.repository.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/favoritos")
public class FavoritoController {
    private final FavoritoRepository favRepo;
    private final AlunoRepository alunoRepo;
    private final TreinoRepository treinoRepo;


    public FavoritoController(FavoritoRepository favRepo, AlunoRepository alunoRepo, TreinoRepository treinoRepo) {
        this.favRepo = favRepo; this.alunoRepo = alunoRepo; this.treinoRepo = treinoRepo;
    }


    @GetMapping
    public List<Favorito> all() { return favRepo.findAll(); }


    @GetMapping("/aluno/{alunoId}")
    public List<Favorito> byAluno(@PathVariable Long alunoId) { return favRepo.findByAlunoId(alunoId); }


    @PostMapping
    public ResponseEntity<Favorito> create(@RequestParam Long alunoId, @RequestParam Long treinoId) {
        Optional<Aluno> a = alunoRepo.findById(alunoId);
        Optional<Treino> t = treinoRepo.findById(treinoId);
        if (a.isPresent() && t.isPresent()) {
            Favorito f = new Favorito(a.get(), t.get());
            favRepo.save(f);
            return ResponseEntity.ok(f);
        }
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (favRepo.existsById(id)) { favRepo.deleteById(id); return ResponseEntity.noContent().build(); }
        return ResponseEntity.notFound().build();
    }
}