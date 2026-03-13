package com.fitbridge.controller;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fitbridge.dto.FavoritoDTO;
import com.fitbridge.dto.SavedTreinoDTO;
import com.fitbridge.model.*;
import com.fitbridge.repository.*;
import com.fitbridge.util.TreinoConverter;
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
    public List<FavoritoDTO> all() { 
        return favRepo.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }


    @GetMapping("/{id}")
    public ResponseEntity<FavoritoDTO> getById(@PathVariable Long id) {
        Optional<Favorito> fav = favRepo.findById(id);
        if (fav.isPresent()) {
            return ResponseEntity.ok(convertToDTO(fav.get()));
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/aluno/{alunoId}")
    public List<FavoritoDTO> byAluno(@PathVariable Long alunoId) { 
        return favRepo.findByAlunoId(alunoId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
    }


    /**
     * Cria um novo favorito e salva uma cópia completa dos dados do treino.
     * Os dados do treino serão armazenados em JSON para permitir edição local.
     */
    @PostMapping
    public ResponseEntity<FavoritoDTO> create(@RequestParam Long alunoId, @RequestParam Long treinoId) {
        Optional<Aluno> a = alunoRepo.findById(alunoId);
        Optional<Treino> t = treinoRepo.findById(treinoId);
        if (a.isPresent() && t.isPresent()) {
            Favorito f = new Favorito(a.get(), t.get());
            
            // Serializar os dados completos do treino em JSON
            String treinoDadosJson = TreinoConverter.treinoToJson(t.get());
            f.setTreinoDados(treinoDadosJson);
            
            favRepo.save(f);
            return ResponseEntity.ok(convertToDTO(f));
        }
        return ResponseEntity.notFound().build();
    }


    /**
     * Atualiza os dados locais de um treino favorito.
     * Permite que o usuário modifique a cópia salva localmente do treino.
     */
    @PutMapping("/{id}")
    public ResponseEntity<FavoritoDTO> updateTreinoData(@PathVariable Long id, @RequestBody SavedTreinoDTO novosTreinoDados) {
        Optional<Favorito> favorito = favRepo.findById(id);
        if (favorito.isPresent()) {
            Favorito f = favorito.get();
            
            // Serializar os novos dados do treino
            String treinoDadosJson = TreinoConverter.serializeSavedTreino(novosTreinoDados);
            f.setTreinoDados(treinoDadosJson);
            
            favRepo.save(f);
            return ResponseEntity.ok(convertToDTO(f));
        }
        return ResponseEntity.notFound().build();
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        if (favRepo.existsById(id)) { favRepo.deleteById(id); return ResponseEntity.noContent().build(); }
        return ResponseEntity.notFound().build();
    }


    /**
     * Converte uma entidade Favorito em FavoritoDTO com dados desserializados.
     */
    private FavoritoDTO convertToDTO(Favorito favorito) {
        SavedTreinoDTO treinoDados = null;
        
        // Desserializar os dados do treino se existirem
        if (favorito.getTreinoDados() != null && !favorito.getTreinoDados().isEmpty()) {
            treinoDados = TreinoConverter.deserializeSavedTreino(favorito.getTreinoDados());
        }
        
        return new FavoritoDTO(
            favorito.getId(),
            favorito.getAluno().getId(),
            favorito.getDataInclusao(),
            treinoDados
        );
    }
}