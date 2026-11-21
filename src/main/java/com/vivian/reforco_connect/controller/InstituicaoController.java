package com.vivian.reforco_connect.controller;

import com.vivian.reforco_connect.domain.Instituicao;
import com.vivian.reforco_connect.service.InstituicaoService;
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
@RequestMapping("/instituicoes")
public class InstituicaoController {

    @Autowired
    private InstituicaoService service;

    @GetMapping
    public List<Instituicao> listar() {
        return service.listar();
    }

    // 404 (via GlobalExceptionHandler) se o id não existir
    @GetMapping("/{id}")
    public ResponseEntity<Instituicao> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @PostMapping
    public ResponseEntity<Instituicao> cadastrar(@Valid @RequestBody Instituicao instituicao) {
        Instituicao instituicaoSalva = service.cadastrar(instituicao);
        return ResponseEntity.status(HttpStatus.CREATED).body(instituicaoSalva);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Instituicao> atualizar(@PathVariable Long id, @Valid @RequestBody Instituicao instituicaoAtualizada) {
        return ResponseEntity.ok(service.atualizar(id, instituicaoAtualizada));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.remover(id);
        return ResponseEntity.noContent().build();
    }
}
