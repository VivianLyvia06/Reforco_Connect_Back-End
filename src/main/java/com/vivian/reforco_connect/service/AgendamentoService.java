package com.vivian.reforco_connect.service;

import com.vivian.reforco_connect.domain.Agendamento;
import com.vivian.reforco_connect.domain.Professor;
import com.vivian.reforco_connect.exception.RecursoNaoEncontradoException;
import com.vivian.reforco_connect.repository.AgendamentoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class AgendamentoService {

    @Autowired
    private AgendamentoRepository repository;

    @Autowired
    private ProfessorService professorService;

    public List<Agendamento> listar() {
        return repository.findAll();
    }

    public Agendamento buscarPorId(Long id) {
        Optional<Agendamento> agendamentoEncontrado = repository.findById(id);
        if (agendamentoEncontrado.isEmpty()) {
            throw new RecursoNaoEncontradoException("Agendamento", id);
        }
        return agendamentoEncontrado.get();
    }

    @Transactional
    public Agendamento cadastrar(Agendamento agendamento) {
        agendamento.setId(null);
        agendamento.setProfessor(buscarProfessorInformado(agendamento));
        return repository.save(agendamento);
    }

    @Transactional
    public Agendamento atualizar(Long id, Agendamento agendamentoAtualizado) {
        Agendamento agendamento = buscarPorId(id);
        agendamento.setAlunoNome(agendamentoAtualizado.getAlunoNome());
        agendamento.setDataHora(agendamentoAtualizado.getDataHora());
        agendamento.setDuracaoMinutos(agendamentoAtualizado.getDuracaoMinutos());
        agendamento.setStatus(agendamentoAtualizado.getStatus());
        // Permite remarcar o agendamento para outro professor
        agendamento.setProfessor(buscarProfessorInformado(agendamentoAtualizado));
        return repository.save(agendamento);
    }

    @Transactional
    public void remover(Long id) {
        Agendamento agendamento = buscarPorId(id);
        repository.delete(agendamento);
    }

    private Professor buscarProfessorInformado(Agendamento agendamento) {
        Long professorId = agendamento.getProfessor().getId();
        if (professorId == null) {
            throw new RecursoNaoEncontradoException("Professor", null);
        }
        return professorService.buscarPorId(professorId);
    }
}
