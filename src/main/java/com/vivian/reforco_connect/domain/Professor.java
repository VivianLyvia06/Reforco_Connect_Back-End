package com.vivian.reforco_connect.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "professor")
public class Professor {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome do professor é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @NotBlank(message = "A disciplina é obrigatória")
    @Size(max = 50, message = "A disciplina deve ter no máximo 50 caracteres")
    @Column(nullable = false, length = 50)
    private String disciplina;

    @Column(name = "foto_url")
    private String fotoUrl;

    // Não serializado (ver comentário em Instituicao.professores)
    @OneToMany(mappedBy = "professor", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Agendamento> agendamentos;

    // Chave Estrangeira apontando para Instituicao (N:1).
    // Este lado É serializado: GET /professores devolve a instituição aninhada,
    // e o POST/PUT aceita {"instituicao": {"id": 1}}.
    @NotNull(message = "A instituição é obrigatória")
    @ManyToOne
    @JoinColumn(name = "instituicao_id", nullable = false)
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
