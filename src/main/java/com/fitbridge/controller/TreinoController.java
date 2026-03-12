package com.fitbridge.controller;


import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.*;


import com.fitbridge.model.*;
import com.fitbridge.repository.*;


@RestController
@RequestMapping("/api/treinos")
public class TreinoController {
    private final TreinoRepository treinoRepo;
    private final ExercicioRepository exRepo;
    private final InstrutorRepository instrRepo;

    public TreinoController(TreinoRepository treinoRepo, ExercicioRepository exRepo, InstrutorRepository instrRepo) {
        this.treinoRepo = treinoRepo; this.exRepo = exRepo; this.instrRepo = instrRepo;
    }


    @GetMapping
    public List<Treino> all() { return treinoRepo.findAll(); }


    @GetMapping("/{id}")
    public ResponseEntity<Treino> get(@PathVariable Long id) {
        return treinoRepo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<Treino> create(@RequestBody Treino treino) {
        // resolve instrutor reference
        if (treino.getInstrutor()!=null && treino.getInstrutor().getId()!=null) {
            instrRepo.findById(treino.getInstrutor().getId()).ifPresent(treino::setInstrutor);
        }
        // prepare itens (join entities)
        if (treino.getItens() != null) {
            for (TreinoExercicio te : treino.getItens()) {
                if (te.getExercicio() != null && te.getExercicio().getId() != null) {
                    exRepo.findById(te.getExercicio().getId()).ifPresent(te::setExercicio);
                }
                te.setTreino(treino);
            }
        }
        Treino saved = treinoRepo.save(treino);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/bulk")
    public List<Treino> createBulk(@RequestBody List<Treino> treinos) {
        for (Treino t : treinos) {
            if (t.getItens() != null && !t.getItens().isEmpty()) {
                for (TreinoExercicio te : t.getItens()) {
                    if (te.getExercicio() != null && te.getExercicio().getId() != null) {
                        exRepo.findById(te.getExercicio().getId()).ifPresent(te::setExercicio);
                    }
                    te.setTreino(t);
                }
            }
            if (t.getInstrutor()!=null && t.getInstrutor().getId()!=null) {
                instrRepo.findById(t.getInstrutor().getId()).ifPresent(t::setInstrutor);
            }
        }
        return treinoRepo.saveAll(treinos);
    }


    @PostMapping("/{id}/exercicios")
    public ResponseEntity<Treino> addExercicio(@PathVariable Long id, @RequestBody TreinoExercicio teReq) {
        Optional<Treino> ot = treinoRepo.findById(id);
        if (ot.isPresent() && teReq.getExercicio()!=null && teReq.getExercicio().getId()!=null) {
            Optional<Exercicio> oe = exRepo.findById(teReq.getExercicio().getId());
            if (oe.isPresent()) {
                Treino t = ot.get();
                TreinoExercicio te = new TreinoExercicio();
                te.setTreino(t);
                te.setExercicio(oe.get());
                te.setSeries(teReq.getSeries());
                te.setRepeticoes(teReq.getRepeticoes());
                t.getItens().add(te);
                treinoRepo.save(t);
                return ResponseEntity.ok(t);
            }
        }
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}/exercicios/{exId}")
    public ResponseEntity<Treino> removeExercicio(@PathVariable Long id, @PathVariable Long exId) {
        Optional<Treino> ot = treinoRepo.findById(id);
        if (ot.isPresent()) {
            Treino t = ot.get();
            t.getItens().removeIf(te -> te.getExercicio()!=null && te.getExercicio().getId().equals(exId));
            treinoRepo.save(t);
            return ResponseEntity.ok(t);
        }
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (treinoRepo.existsById(id)) { treinoRepo.deleteById(id); return ResponseEntity.noContent().build(); }
        return ResponseEntity.notFound().build();
    }
}