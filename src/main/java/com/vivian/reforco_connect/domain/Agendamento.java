package com.vivian.reforco_connect.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import java.time.LocalDateTime;

@Entity
@Table(name = "agendamento")
public class Agendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull(message = "A data e hora são obrigatórias")
    @Column(name = "data_hora", nullable = false)
    private LocalDateTime dataHora;

    // columnDefinition com DEFAULT 60: com ddl-auto=update o Hibernate faz
    // ALTER TABLE ... ADD COLUMN. Sem o DEFAULT no banco, o Postgres recusaria
    // a coluna NOT NULL numa tabela que já tem linhas.
    // O "= 60" no Java cobre o caso do JSON que não manda o campo.
    @NotNull(message = "A duração é obrigatória")
    @Min(value = 1, message = "A duração deve ser de pelo menos 1 minuto")
    @Column(name = "duracao_minutos", nullable = false, columnDefinition = "integer default 60")
    private Integer duracaoMinutos = 60;

    @NotBlank(message = "O status é obrigatório")
    @Size(max = 20, message = "O status deve ter no máximo 20 caracteres")
    @Column(nullable = false, length = 20)
    private String status;

    @NotBlank(message = "O nome do aluno é obrigatório")
    @Size(max = 100, message = "O nome do aluno deve ter no máximo 100 caracteres")
    @Column(name = "aluno_nome", nullable = false, length = 100)
    private String alunoNome;

    // Serializado: GET /agendamentos devolve o professor (e a instituição dele).
    // Não existe instituicao_id aqui de propósito: a instituição é alcançada
    // via professor -> instituicao. Guardar de novo seria redundância.
    @NotNull(message = "O professor é obrigatório")
    @ManyToOne
    @JoinColumn(name = "professor_id", nullable = false)
    private Professor professor;

    // @JsonCreator no construtor vazio: diz ao Jackson 3 para criar o objeto
    // por aqui e preencher pelos setters. Sem isso ele usa o construtor com
    // parâmetros abaixo e passa null para os campos ausentes no JSON,
    // apagando o valor padrão duracaoMinutos = 60.
    @JsonCreator
    public Agendamento() {}

    public Agendamento(LocalDateTime dataHora, Integer duracaoMinutos, String status, String alunoNome, Professor professor) {
        this.dataHora = dataHora;
        this.duracaoMinutos = duracaoMinutos;
        this.status = status;
        this.alunoNome = alunoNome;
        this.professor = professor;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public LocalDateTime getDataHora() { return dataHora; }
    public void setDataHora(LocalDateTime dataHora) { this.dataHora = dataHora; }

    public Integer getDuracaoMinutos() { return duracaoMinutos; }
    public void setDuracaoMinutos(Integer duracaoMinutos) { this.duracaoMinutos = duracaoMinutos; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getAlunoNome() { return alunoNome; }
    public void setAlunoNome(String alunoNome) { this.alunoNome = alunoNome; }

    public Professor getProfessor() { return professor; }
    public void setProfessor(Professor professor) { this.professor = professor; }
}
