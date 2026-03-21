package com.fitbridge.controller;


import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.util.*;
import java.util.ArrayList;


import com.fitbridge.model.*;
import com.fitbridge.dto.TreinoDTO;
import com.fitbridge.dto.TreinoExercicioDTO;
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
    public ResponseEntity<Treino> create(@RequestBody TreinoDTO dto) {
        Treino treino = new Treino();
        treino.setTitulo(dto.getTitulo());
        treino.setGrupoMuscular(dto.getGrupoMuscular());
        treino.setDescricao(dto.getDescricao());

        // resolve instrutor reference
        if (dto.getInstrutorId() != null) {
            instrRepo.findById(dto.getInstrutorId()).ifPresent(treino::setInstrutor);
        }

        // prepare itens from DTO exercise IDs
        if (dto.getItens() != null) {
            for (TreinoExercicioDTO itemDTO : dto.getItens()) {
                if (itemDTO.getExercicioId() != null) {
                    Optional<Exercicio> oe = exRepo.findById(itemDTO.getExercicioId());
                    if (oe.isPresent()) {
                        TreinoExercicio te = new TreinoExercicio();
                        te.setTreino(treino);
                        te.setExercicio(oe.get());
                        te.setSeries(itemDTO.getSeries());
                        te.setRepeticoes(itemDTO.getRepeticoes());
                        treino.getItens().add(te);
                    }
                }
            }
        }

        Treino saved = treinoRepo.save(treino);
        return ResponseEntity.ok(saved);
    }

    @PostMapping("/bulk")
    public List<Treino> createBulk(@RequestBody List<TreinoDTO> dtos) {
        List<Treino> treinos = new ArrayList<>();
        for (TreinoDTO dto : dtos) {
            Treino t = new Treino();
            t.setTitulo(dto.getTitulo());
            t.setGrupoMuscular(dto.getGrupoMuscular());
            t.setDescricao(dto.getDescricao());

            if (dto.getInstrutorId() != null) {
                instrRepo.findById(dto.getInstrutorId()).ifPresent(t::setInstrutor);
            }

            if (dto.getItens() != null) {
                for (TreinoExercicioDTO itemDTO : dto.getItens()) {
                    if (itemDTO.getExercicioId() != null) {
                        exRepo.findById(itemDTO.getExercicioId()).ifPresent(ex -> {
                            TreinoExercicio te = new TreinoExercicio();
                            te.setTreino(t);
                            te.setExercicio(ex);
                            te.setSeries(itemDTO.getSeries());
                            te.setRepeticoes(itemDTO.getRepeticoes());
                            t.getItens().add(te);
                        });
                    }
                }
            }
            treinos.add(t);
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