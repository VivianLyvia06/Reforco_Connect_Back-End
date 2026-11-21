package com.vivian.reforco_connect.service;

import com.vivian.reforco_connect.domain.Instituicao;
import com.vivian.reforco_connect.domain.Professor;
import com.vivian.reforco_connect.dto.RelatorioEngajamento;
import com.vivian.reforco_connect.exception.RecursoNaoEncontradoException;
import com.vivian.reforco_connect.repository.ProfessorRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class ProfessorService {

    @Autowired
    private ProfessorRepository repository;

    @Autowired
    private InstituicaoService instituicaoService;

    public List<Professor> listar() {
        return repository.findAll();
    }

    public Professor buscarPorId(Long id) {
        Optional<Professor> professorEncontrado = repository.findById(id);
        if (professorEncontrado.isEmpty()) {
            throw new RecursoNaoEncontradoException("Professor", id);
        }
        return professorEncontrado.get();
    }

    public List<RelatorioEngajamento> relatorioEngajamento() {
        return repository.relatorioEngajamento();
    }

    @Transactional
    public Professor cadastrar(Professor professor) {
        professor.setId(null);
        // O JSON traz só {"instituicao": {"id": 1}}. Buscamos a instituição
        // de verdade no banco: se o id não existir, a resposta é 404.
        professor.setInstituicao(buscarInstituicaoInformada(professor));
        return repository.save(professor);
    }

    @Transactional
    public Professor atualizar(Long id, Professor professorAtualizado) {
        Professor professor = buscarPorId(id);
        professor.setNome(professorAtualizado.getNome());
        professor.setDisciplina(professorAtualizado.getDisciplina());
        professor.setFotoUrl(professorAtualizado.getFotoUrl());
        // Permite transferir o professor para outra instituição
        professor.setInstituicao(buscarInstituicaoInformada(professorAtualizado));
        return repository.save(professor);
    }

    @Transactional
    public void remover(Long id) {
        // Pelo cascade ALL, remove também os agendamentos do professor.
        Professor professor = buscarPorId(id);
        repository.delete(professor);
    }

    private Instituicao buscarInstituicaoInformada(Professor professor) {
        Long instituicaoId = professor.getInstituicao().getId();
        if (instituicaoId == null) {
            throw new RecursoNaoEncontradoException("Instituição", null);
        }
        return instituicaoService.buscarPorId(instituicaoId);
    }
}
