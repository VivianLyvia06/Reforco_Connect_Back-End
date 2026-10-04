package com.vivian.reforco_connect.controller;

import com.vivian.reforco_connect.domain.Instituicao;
import com.vivian.reforco_connect.repository.InstituicaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/instituicoes")
public class InstituicaoController {

    @Autowired
    private InstituicaoRepository repository;

    @GetMapping
    public List<Instituicao> listar() {
        return repository.findAll();
    }

    @PostMapping
    public Instituicao cadastrar(@RequestBody Instituicao instituicao) {
        return repository.save(instituicao);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Instituicao> atualizar(@PathVariable Long id, @RequestBody Instituicao instituicaoAtualizada) {
        return repository.findById(id)
                .map(instituicao -> {
                    instituicao.setNome(instituicaoAtualizada.getNome());
                    instituicao.setTelefone(instituicaoAtualizada.getTelefone());
                    instituicao.setEndereco(instituicaoAtualizada.getEndereco());
                    Instituicao salva = repository.save(instituicao);
                    return ResponseEntity.ok(salva);
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