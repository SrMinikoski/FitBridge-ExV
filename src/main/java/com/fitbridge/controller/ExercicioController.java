package com.fitbridge.controller;


import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;


import com.fitbridge.model.Exercicio;
import com.fitbridge.dto.ExercicioDTO;
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
    public ResponseEntity<?> create(@RequestBody ExercicioDTO dto) {
        try {
            Exercicio ex = new Exercicio(
                dto.getNome(),
                dto.getDescricao(),
                dto.getMusculoAlvo(),
                dto.getMusculosAuxiliares(),
                dto.getDiretorioImagem()
            );
            Exercicio saved = repo.save(ex);
            return ResponseEntity.ok(saved);
        } catch (Exception e) {
            e.printStackTrace();
            Map<String, String> error = new HashMap<>();
            error.put("erro", "Erro ao salvar exercício: " + e.getMessage());
            return ResponseEntity.status(500).body(error);
        }
    }


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

    @PostMapping("/upload-exercise-image")
    public ResponseEntity<Map<String, String>> uploadExerciseImage(@RequestParam("file") MultipartFile file) {
        try {
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body(Map.of("erro", "Nenhum arquivo enviado."));
            }

            // Diretório raiz do projeto
            String projectRoot = System.getProperty("user.dir");
            Path uploadDir = Paths.get(projectRoot, "public", "exercises");

            // Criar diretório se não existir
            if (!Files.exists(uploadDir)) {
                Files.createDirectories(uploadDir);
            }

            // Salvar arquivo
            String filename = file.getOriginalFilename();
            Path filePath = uploadDir.resolve(filename);
            Files.write(filePath, file.getBytes());

            // Retornar caminho relativo para o frontend
            String relativePath = "exercises/" + filename;
            
            Map<String, String> response = new HashMap<>();
            response.put("sucesso", "true");
            response.put("filePath", relativePath);
            response.put("nomeArquivo", filename);

            return ResponseEntity.ok(response);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("erro", "Erro ao salvar arquivo: " + e.getMessage()));
        }
    }
}