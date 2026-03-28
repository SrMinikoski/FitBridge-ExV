package com.fitbridge.controller;


import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import java.text.Normalizer;
import java.util.*;

import com.fitbridge.model.*;
import com.fitbridge.dto.TreinoDTO;
import com.fitbridge.dto.TreinoExercicioDTO;
import com.fitbridge.repository.*;
import com.fitbridge.util.AuthValidationUtil;


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


    // Função 3: listar todos os treinos criados por determinado instrutor
    @GetMapping("/instrutor/{instrutorId}")
    public ResponseEntity<List<Treino>> byInstrutor(@PathVariable Long instrutorId) {
        List<Treino> treinos = treinoRepo.findByInstrutorId(instrutorId);
        return ResponseEntity.ok(treinos);
    }


    // Normaliza string: remove acentos e converte para minúsculas
    private String normalizar(String s) {
        if (s == null) return "";
        return Normalizer.normalize(s, Normalizer.Form.NFD)
                .replaceAll("\\p{InCombiningDiacriticalMarks}", "")
                .toLowerCase();
    }

    // Função 4: pesquisar treinos por título e/ou músculo alvo (case e accent insensitive)
    @GetMapping("/buscar")
    public ResponseEntity<?> buscar(@RequestParam String q) {
        if (q == null || q.trim().isEmpty()) {
            return ResponseEntity.badRequest().body(Map.of("error", "Parâmetro de busca 'q' não pode ser vazio."));
        }
        // Normaliza o termo buscado: remove acentos e converte pra minúsculo
        String termo = normalizar(q.trim());
        // Filtra em Java — não depende de colunas extras no banco
        List<Treino> resultado = treinoRepo.findAll().stream()
                .filter(t -> normalizar(t.getTitulo()).contains(termo)
                          || normalizar(t.getGrupoMuscular()).contains(termo))
                .collect(java.util.stream.Collectors.toList());
        return ResponseEntity.ok(resultado);
    }


    @GetMapping("/{id}")
    public ResponseEntity<Treino> get(@PathVariable Long id) {
        return treinoRepo.findById(id).map(ResponseEntity::ok).orElse(ResponseEntity.notFound().build());
    }


    @PostMapping
    public ResponseEntity<?> create(
            @RequestBody TreinoDTO dto,
            @RequestHeader(value = "X-User-ID", required = false) String userId,
            @RequestHeader(value = "X-User-Type", required = false) String userType) {
        
        // Validar autenticação
        ResponseEntity<?> authError = AuthValidationUtil.validateUserAuth(userId, userType);
        if (authError != null) return authError;
        
        // Apenas instrutores podem criar treinos
        if (!"INSTRUTOR".equals(userType)) {
            return ResponseEntity.status(403).body(Map.of("error", "Apenas instrutores podem criar treinos."));
        }
        
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
    public ResponseEntity<?> createBulk(
            @RequestBody List<TreinoDTO> dtos,
            @RequestHeader(value = "X-User-ID", required = false) String userId,
            @RequestHeader(value = "X-User-Type", required = false) String userType) {
        
        // Validar autenticação
        ResponseEntity<?> authError = AuthValidationUtil.validateUserAuth(userId, userType);
        if (authError != null) return authError;
        
        // Apenas instrutores podem criar treinos
        if (!"INSTRUTOR".equals(userType)) {
            return ResponseEntity.status(403).body(Map.of("error", "Apenas instrutores podem criar treinos em bulk."));
        }
        
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
        return ResponseEntity.ok(treinoRepo.saveAll(treinos));
    }


    @PostMapping("/{id}/exercicios")
    public ResponseEntity<?> addExercicio(
            @PathVariable Long id,
            @RequestBody TreinoExercicio teReq,
            @RequestHeader(value = "X-User-ID", required = false) String userId,
            @RequestHeader(value = "X-User-Type", required = false) String userType) {
        
        // Validar autenticação
        ResponseEntity<?> authError = AuthValidationUtil.validateUserAuth(userId, userType);
        if (authError != null) return authError;
        
        // Apenas instrutores podem adicionar exercícios
        if (!"INSTRUTOR".equals(userType)) {
            return ResponseEntity.status(403).body(Map.of("error", "Apenas instrutores podem adicionar exercícios."));
        }
        
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
    public ResponseEntity<?> removeExercicio(
            @PathVariable Long id,
            @PathVariable Long exId,
            @RequestHeader(value = "X-User-ID", required = false) String userId,
            @RequestHeader(value = "X-User-Type", required = false) String userType) {
        
        // Validar autenticação
        ResponseEntity<?> authError = AuthValidationUtil.validateUserAuth(userId, userType);
        if (authError != null) return authError;
        
        // Apenas instrutores
        if (!"INSTRUTOR".equals(userType)) {
            return ResponseEntity.status(403).body(Map.of("error", "Apenas instrutores podem remover exercícios."));
        }
        
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
    public ResponseEntity<?> delete(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-ID", required = false) String userId,
            @RequestHeader(value = "X-User-Type", required = false) String userType) {
        
        // Validar autenticação
        ResponseEntity<?> authError = AuthValidationUtil.validateUserAuth(userId, userType);
        if (authError != null) return authError;
        
        // Apenas instrutores
        if (!"INSTRUTOR".equals(userType)) {
            return ResponseEntity.status(403).body(Map.of("error", "Apenas instrutores podem deletar treinos."));
        }
        
        if (treinoRepo.existsById(id)) { 
            treinoRepo.deleteById(id); 
            return ResponseEntity.noContent().build(); 
        }
        return ResponseEntity.notFound().build();
    }
}