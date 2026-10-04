package com.vivian.reforco_connect.controller;

import com.vivian.reforco_connect.domain.Agendamento;
import com.vivian.reforco_connect.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;


import java.util.List;

@RestController
@RequestMapping("/agendamentos")
public class AgendamentoController {

    @Autowired
    private AgendamentoRepository repository;

    @GetMapping
    public List<Agendamento> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Agendamento cadastrar(@RequestBody Agendamento agendamento) {
        return repository.save(agendamento);
    }
    @PutMapping("/{id}")
    public ResponseEntity<Agendamento> atualizar(@PathVariable Long id, @RequestBody Agendamento agendamentoAtualizado) {
        return repository.findById(id)
                .map(agendamento -> {
                    agendamento.setAlunoNome(agendamentoAtualizado.getAlunoNome());
                    agendamento.setDataHora(agendamentoAtualizado.getDataHora());
                    agendamento.setStatus(agendamentoAtualizado.getStatus());
                    Agendamento salvo = repository.save(agendamento);
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