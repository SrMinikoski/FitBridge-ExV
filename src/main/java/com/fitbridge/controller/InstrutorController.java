package com.fitbridge.controller;


import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.List;


import com.fitbridge.model.Instrutor;
import com.fitbridge.repository.InstrutorRepository;


@RestController
@RequestMapping("/api/instrutores")
public class InstrutorController {
    private final InstrutorRepository repo;
    public InstrutorController(InstrutorRepository repo) { this.repo = repo; }


    @GetMapping
    public List<Instrutor> all() { return repo.findAll(); }


    @GetMapping("/{id}")
    public ResponseEntity<Instrutor> get(@PathVariable Long id) { return repo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build()); }


    @PostMapping
    public Instrutor create(@RequestBody Instrutor instr) { return repo.save(instr); }

    @PostMapping("/bulk")
    public List<Instrutor> createBulk(@RequestBody List<Instrutor> instrutores) { return repo.saveAll(instrutores); }

    @PutMapping("/{id}")
    public ResponseEntity<Instrutor> update(@PathVariable Long id, @RequestBody Instrutor instr) {
        return repo.findById(id).map(i -> {
            i.setNome(instr.getNome()); i.setSexo(instr.getSexo()); i.setIdade(instr.getIdade());
            i.setCrefDiploma(instr.getCrefDiploma()); i.setEmail(instr.getEmail());
            repo.save(i); return ResponseEntity.ok(i);
        }).orElse(ResponseEntity.notFound().build());
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (repo.existsById(id)) { repo.deleteById(id); return ResponseEntity.noContent().build(); }
        return ResponseEntity.notFound().build();
    }
}