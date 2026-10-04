package com.vivian.reforco_connect.domain;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import java.util.List;

@Entity
@Table(name = "professor")
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do professor é obrigatório")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "A disciplina é obrigatória")
    @Column(nullable = false, length = 50)
    private String disciplina;

    @Column(name = "foto_url")
    private String fotoUrl;

    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
    @com.fasterxml.jackson.annotation.JsonManagedReference
    private List<Agendamento> agendamentos;
    // Chave Estrangeira apontando para Instituicao (N:1)
    @ManyToOne
    @JoinColumn(name = "instituicao_id", nullable = false)
    @com.fasterxml.jackson.annotation.JsonBackReference
    private Instituicao instituicao;

    public Professor() {}

    public Professor(String nome, String disciplina, String fotoUrl, Instituicao instituicao) {
        this.nome = nome;
        this.disciplina = disciplina;
        this.fotoUrl = fotoUrl;
        this.instituicao = instituicao;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getDisciplina() { return disciplina; }
    public void setDisciplina(String disciplina) { this.disciplina = disciplina; }

    public String getFotoUrl() { return fotoUrl; }
    public void setFotoUrl(String fotoUrl) { this.fotoUrl = fotoUrl; }

    public Instituicao getInstituicao() { return instituicao; }
    public void setInstituicao(Instituicao instituicao) { this.instituicao = instituicao; }

    public List<Agendamento> getAgendamentos() { return agendamentos; }
    public void setAgendamentos(List<Agendamento> agendamentos) { this.agendamentos = agendamentos; }
}