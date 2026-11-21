package com.vivian.reforco_connect.controller;

import com.vivian.reforco_connect.domain.Agendamento;
import com.vivian.reforco_connect.service.AgendamentoService;
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
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoService service;

    @GetMapping
    public List<Agendamento> listar() {
        return service.listar();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Agendamento> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    // Corpo: {"alunoNome": "...", "dataHora": "2026-11-25T14:30:00",
    //         "duracaoMinutos": 60, "status": "Agendado", "professor": {"id": 1}}
    @PostMapping
    public ResponseEntity<Agendamento> cadastrar(@Valid @RequestBody Agendamento agendamento) {
        Agendamento agendamentoSalvo = service.cadastrar(agendamento);
        return ResponseEntity.status(HttpStatus.CREATED).body(agendamentoSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Agendamento> atualizar(@PathVariable Long id, @Valid @RequestBody Agendamento agendamentoAtualizado) {
        return ResponseEntity.ok(service.atualizar(id, agendamentoAtualizado));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
