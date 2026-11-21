package com.vivian.reforco_connect.service;

import com.vivian.reforco_connect.domain.Instituicao;
import com.vivian.reforco_connect.exception.RecursoNaoEncontradoException;
import com.vivian.reforco_connect.repository.InstituicaoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class InstituicaoService {

    @Autowired
    private InstituicaoRepository repository;

    public List<Instituicao> listar() {
        return repository.findAll();
    }

    public Instituicao buscarPorId(Long id) {
        Optional<Instituicao> instituicaoEncontrada = repository.findById(id);
        if (instituicaoEncontrada.isEmpty()) {
            throw new RecursoNaoEncontradoException("Instituição", id);
        }
        return instituicaoEncontrada.get();
    }

    @Transactional
    public Instituicao cadastrar(Instituicao instituicao) {
        // Garante INSERT (ignora um id vindo no JSON)
        instituicao.setId(null);
        return repository.save(instituicao);
    }

    @Transactional
    public Instituicao atualizar(Long id, Instituicao instituicaoAtualizada) {
        Instituicao instituicao = buscarPorId(id);
        instituicao.setNome(instituicaoAtualizada.getNome());
        instituicao.setTelefone(instituicaoAtualizada.getTelefone());
        instituicao.setEndereco(instituicaoAtualizada.getEndereco());
        return repository.save(instituicao);
    }

    @Transactional
    public void remover(Long id) {
        // Busca antes para devolver 404 se não existir.
        // Pelo cascade ALL, remove também os professores e os agendamentos deles.
        Instituicao instituicao = buscarPorId(id);
        repository.delete(instituicao);
    }
}
