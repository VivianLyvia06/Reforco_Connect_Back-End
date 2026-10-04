package com.vivian.reforco_connect.domain;

import jakarta.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "agendamento")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    @Column(nullable = false, length = 20)
    private String status;

    @Column(name = "aluno_nome", nullable = false, length = 100)
    private String alunoNome;

    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonBackReference
    private Professor professor;

    public Agendamento() {}

    public Agendamento(LocalDateTime dataHora, String status, String alunoNome, Professor professor) {
        this.dataHora = dataHora;
        this.status = status;
        this.alunoNome = alunoNome;
        this.professor = professor;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAlunoNome() { return alunoNome; }
    public void setAlunoNome(String alunoNome) { this.alunoNome = alunoNome; }

    public Professor getProfessor() { return professor; }
    public void setProfessor(Professor professor) { this.professor = professor; }
}