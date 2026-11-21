package com.vivian.reforco_connect.controller;

import com.vivian.reforco_connect.domain.Professor;
import com.vivian.reforco_connect.dto.RelatorioEngajamento;
import com.vivian.reforco_connect.service.ProfessorService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/professores")
public class ProfessorController {

    @Autowired
    private ProfessorService service;

    // Endpoint para buscar todos os professores (GET)
    @GetMapping
    public List<Professor> listar() {
        return service.listar();
    }

    // Endpoint de agregação (native query com COUNT/SUM + GROUP BY).
    // Não conflita com /{id}: o Spring prioriza o caminho literal sobre o
    // path variável. Além disso, {id} é Long e "relatorio-engajamento" não é.
    @GetMapping("/relatorio-engajamento")
    public List<RelatorioEngajamento> relatorioEngajamento() {
        return service.relatorioEngajamento();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Professor> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // Endpoint para cadastrar um novo professor (POST)
    // Corpo: {"nome": "...", "disciplina": "...", "fotoUrl": "...", "instituicao": {"id": 1}}
    @PostMapping
    public ResponseEntity<Professor> cadastrar(@Valid @RequestBody Professor professor) {
        Professor professorSalvo = service.cadastrar(professor);
        return ResponseEntity.status(HttpStatus.CREATED).body(professorSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Professor> atualizar(@PathVariable Long id, @Valid @RequestBody Professor professorAtualizado) {
        return ResponseEntity.ok(service.atualizar(id, professorAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
