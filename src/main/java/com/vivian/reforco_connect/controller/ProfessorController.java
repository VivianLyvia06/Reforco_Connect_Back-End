package com.vivian.reforco_connect.controller;

import com.vivian.reforco_connect.domain.Professor;
import com.vivian.reforco_connect.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/professores")
public class ProfessorController {

    @Autowired
    private ProfessorRepository repository;

    // Endpoint para buscar todos os professores (GET)
    @GetMapping
    public List<Professor> listar() {
        return repository.findAll();
    }

    // Endpoint para cadastrar um novo professor (POST)
    @PostMapping
    public Professor cadastrar(@RequestBody Professor professor) {
        return repository.save(professor);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Professor> atualizar(@PathVariable Long id, @RequestBody Professor professorAtualizado) {
        return repository.findById(id)
                .map(professor -> {
                    professor.setNome(professorAtualizado.getNome());
                    professor.setDisciplina(professorAtualizado.getDisciplina());
                    professor.setFotoUrl(professorAtualizado.getFotoUrl());
                    Professor salvo = repository.save(professor);
                    return ResponseEntity.ok(salvo);
                })
                .orElse(ResponseEntity.notFound().build());
    }
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        if (repository.existsById(id)) {
            repository.deleteById(id);
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.notFound().build();
    }
}