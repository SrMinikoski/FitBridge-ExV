package com.fitbridge.controller;


import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;


import com.fitbridge.model.Aluno;
import com.fitbridge.repository.AlunoRepository;


@RestController
@RequestMapping("/api/alunos")
public class AlunoController {
    private final AlunoRepository repo;
    public AlunoController(AlunoRepository repo) { this.repo = repo; }


    @GetMapping
    public List<Aluno> all() { return repo.findAll(); }


    @GetMapping("/{id}")
    public ResponseEntity<Aluno> get(@PathVariable Long id) { return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }


    @PostMapping
    public Aluno create(@RequestBody Aluno aluno) { return repo.save(aluno); }

    @PostMapping("/bulk")
    public List<Aluno> createBulk(@RequestBody List<Aluno> alunos) { return repo.saveAll(alunos); }

    @PutMapping("/{id}")
    public ResponseEntity<Aluno> update(@PathVariable Long id, @RequestBody Aluno aluno) {
        return repo.findById(id).map(a -> {
            a.setNome(aluno.getNome()); a.setSexo(aluno.getSexo()); a.setIdade(aluno.getIdade());
            a.setAltura(aluno.getAltura()); a.setPeso(aluno.getPeso()); a.setObjetivo(aluno.getObjetivo());
            a.setEmail(aluno.getEmail()); repo.save(a); return ResponseEntity.ok(a);
        }).orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (repo.existsById(id)) { repo.deleteById(id); return ResponseEntity.noContent().build(); }
        return ResponseEntity.notFound().build();
    }
}