package com.fitbridge.controller;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.fitbridge.dto.FavoritoDTO;
import com.fitbridge.dto.SavedTreinoDTO;
import com.fitbridge.model.*;
import com.fitbridge.repository.*;
import com.fitbridge.util.TreinoConverter;
import com.fitbridge.util.AuthValidationUtil;
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
    public ResponseEntity<?> all(
            @RequestHeader(value = "X-User-ID", required = false) String userId,
            @RequestHeader(value = "X-User-Type", required = false) String userType) {
        
        // Validar autenticação
        ResponseEntity<?> authError = AuthValidationUtil.validateUserAuth(userId, userType);
        if (authError != null) return authError;
        
        List<FavoritoDTO> result = favRepo.findAll().stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(result);
    }


    @GetMapping("/{id}")
    public ResponseEntity<?> getById(
            @PathVariable Long id,
            @RequestHeader(value = "X-User-ID", required = false) String userId,
            @RequestHeader(value = "X-User-Type", required = false) String userType) {
        
        // Validar autenticação
        ResponseEntity<?> authError = AuthValidationUtil.validateUserAuth(userId, userType);
        if (authError != null) return authError;
        
        Optional<Favorito> fav = favRepo.findById(id);
        if (fav.isPresent()) {
            return ResponseEntity.ok(convertToDTO(fav.get()));
        }
        return ResponseEntity.notFound().build();
    }


    @GetMapping("/aluno/{alunoId}")
    public ResponseEntity<?> byAluno(
            @PathVariable Long alunoId,
            @RequestHeader(value = "X-User-ID", required = false) String userId,
            @RequestHeader(value = "X-User-Type", required = false) String userType) {
        
        // Validar autenticação
        ResponseEntity<?> authError = AuthValidationUtil.validateUserAuth(userId, userType);
        if (authError != null) return authError;
        
        // Validar se o usuário só acessa seus próprios favoritos
        Long userIdLong = AuthValidationUtil.getUserIdFromHeader(userId);
        if (userIdLong == null || !userIdLong.equals(alunoId)) {
            return ResponseEntity.status(403).body(java.util.Map.of("error", "Acesso negado. Você não pode acessar favoritos de outro usuário."));
        }
        
        List<FavoritoDTO> result = favRepo.findByAlunoId(alunoId).stream()
            .map(this::convertToDTO)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(result);
    }


    /**
     * Cria um novo favorito e salva uma cópia completa dos dados do treino.
     * Os dados do treino serão armazenados em JSON para permitir edição local.
     */
    @PostMapping
    public ResponseEntity<?> create(
            @RequestParam Long alunoId, 
            @RequestParam Long treinoId,
            @RequestHeader(value = "X-User-ID", required = false) String userId,
            @RequestHeader(value = "X-User-Type", required = false) String userType) {
        
        // Validar autenticação
        ResponseEntity<?> authError = AuthValidationUtil.validateUserAuth(userId, userType);
        if (authError != null) return authError;
        
        // Validar se o usuário só cria favoritos para si mesmo
        Long userIdLong = AuthValidationUtil.getUserIdFromHeader(userId);
        if (userIdLong == null || !userIdLong.equals(alunoId)) {
            return ResponseEntity.status(403).body(java.util.Map.of("error", "Acesso negado. Você não pode criar favoritos para outro usuário."));
        }
        
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
    public ResponseEntity<?> updateTreinoData(
            @PathVariable Long id, 
            @RequestBody SavedTreinoDTO novosTreinoDados,
            @RequestHeader(value = "X-User-ID", required = false) String userId,
            @RequestHeader(value = "X-User-Type", required = false) String userType) {
        
        // Validar autenticação
        ResponseEntity<?> authError = AuthValidationUtil.validateUserAuth(userId, userType);
        if (authError != null) return authError;
        
        Optional<Favorito> favorito = favRepo.findById(id);
        if (favorito.isPresent()) {
            Favorito f = favorito.get();
            
            // Validar se o usuário só atualiza seus próprios favoritos
            Long userIdLong = AuthValidationUtil.getUserIdFromHeader(userId);
            if (userIdLong == null || !userIdLong.equals(f.getAluno().getId())) {
                return ResponseEntity.status(403).body(java.util.Map.of("error", "Acesso negado. Você não pode atualizar favoritos de outro usuário."));
            }
            
            // Serializar os novos dados do treino
            String treinoDadosJson = TreinoConverter.serializeSavedTreino(novosTreinoDados);
            f.setTreinoDados(treinoDadosJson);
            
            favRepo.save(f);
            return ResponseEntity.ok(convertToDTO(f));
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
        
        if (favRepo.existsById(id)) {
            Optional<Favorito> favorito = favRepo.findById(id);
            if (favorito.isPresent()) {
                // Validar se o usuário só deleta seus próprios favoritos
                Long userIdLong = AuthValidationUtil.getUserIdFromHeader(userId);
                if (userIdLong == null || !userIdLong.equals(favorito.get().getAluno().getId())) {
                    return ResponseEntity.status(403).body(java.util.Map.of("error", "Acesso negado. Você não pode deletar favoritos de outro usuário."));
                }
            }
            
            favRepo.deleteById(id);
            return ResponseEntity.noContent().build();
        }
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