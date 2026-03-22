package com.fitbridge.controller;

import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;

import com.fitbridge.repository.AlunoRepository;
import com.fitbridge.repository.InstrutorRepository;
import com.fitbridge.model.Aluno;
import com.fitbridge.model.Instrutor;

import java.util.Map;

@RestController
@RequestMapping("/api")
@CrossOrigin(origins = "*")
public class AuthController {
    private final AlunoRepository alunoRepo;
    private final InstrutorRepository instrutorRepo;

    public AuthController(AlunoRepository alunoRepo, InstrutorRepository instrutorRepo) {
        this.alunoRepo = alunoRepo;
        this.instrutorRepo = instrutorRepo;
    }

    public static class LoginRequest {
        public String email;
        public String senha;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest req) {
        if (req == null || req.email == null || req.senha == null) {
            return ResponseEntity.badRequest().body(Map.of("error", "email and senha required"));
        }

        // Try aluno
        return alunoRepo.findByEmail(req.email).map(aluno -> {
            if (req.senha.equals(aluno.getSenha())) {
                return ResponseEntity.ok(Map.of(
                    "id", aluno.getId(),
                    "nome", aluno.getNome(),
                    "email", aluno.getEmail(),
                    "tipo", "ALUNO"
                ));
            }
            return ResponseEntity.status(401).body(Map.of("error", "invalid credentials"));
        }).orElseGet(() -> {
            // Try instrutor
            return instrutorRepo.findByEmail(req.email).map(instrutor -> {
                if (req.senha.equals(instrutor.getSenha())) {
                    return ResponseEntity.ok(Map.of(
                        "id", instrutor.getId(),
                        "nome", instrutor.getNome(),
                        "email", instrutor.getEmail(),
                        "tipo", "INSTRUTOR"
                    ));
                }
                return ResponseEntity.status(401).body(Map.of("error", "invalid credentials"));
            }).orElseGet(() -> ResponseEntity.status(401).body(Map.of("error", "invalid credentials")));
        });
    }
}
