package com.fitbridge.controller;


import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;


import com.fitbridge.model.Exercicio;
import com.fitbridge.repository.ExercicioRepository;


@RestController
@RequestMapping("/api/exercicios")
public class ExercicioController {
    private final ExercicioRepository repo;
    public ExercicioController(ExercicioRepository repo) { this.repo = repo; }


    @GetMapping
    public List<Exercicio> all() { return repo.findAll(); }


    @GetMapping("/{id}")
    public ResponseEntity<Exercicio> get(@PathVariable Long id) {
        return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public Exercicio create(@RequestBody Exercicio ex) { return repo.save(ex); }


    @PutMapping("/{id}")
    public ResponseEntity<Exercicio> update(@PathVariable Long id, @RequestBody Exercicio ex) {
        return repo.findById(id).map(e -> {
            e.setNome(ex.getNome());
            e.setDescricao(ex.getDescricao());
            e.setMusculoAlvo(ex.getMusculoAlvo());
            e.setMusculosAuxiliares(ex.getMusculosAuxiliares());
            e.setDiretorioImagem(ex.getDiretorioImagem());
            repo.save(e);
            return ResponseEntity.ok(e);
        }).orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (repo.existsById(id)) { repo.deleteById(id); return ResponseEntity.noContent().build(); }
        return ResponseEntity.notFound().build();
    }
}