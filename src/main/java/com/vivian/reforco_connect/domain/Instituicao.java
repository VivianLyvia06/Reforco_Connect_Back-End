package com.vivian.reforco_connect.domain;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import java.util.List;

@Entity
@Table(name = "instituicao")
public class Instituicao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank(message = "O nome é obrigatório")
    @Size(max = 100, message = "O nome deve ter no máximo 100 caracteres")
    @Column(nullable = false, length = 100)
    private String nome;

    @Size(max = 20, message = "O telefone deve ter no máximo 20 caracteres")
    @Column(length = 20)
    private String telefone;

    @Size(max = 200, message = "O endereço deve ter no máximo 200 caracteres")
    @Column(length = 200)
    private String endereco;

    // A lista NÃO vai para o JSON: quem aparece no JSON é o lado @ManyToOne
    // (Professor.instituicao). Isso evita a recursão infinita
    // instituicao -> professores -> instituicao -> ...
    // Atenção: cascade ALL => apagar a instituição apaga os professores dela
    // (e, em cascata, os agendamentos desses professores).
    @OneToMany(mappedBy = "instituicao", cascade = CascadeType.ALL)
    @JsonIgnore
    private List<Professor> professores;

    // JPA
    public Instituicao() {}

    public Instituicao(String nome, String telefone, String endereco) {
        this.nome = nome;
        this.telefone = telefone;
        this.endereco = endereco;
    }

    // Getters e Setters
    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public String getNome() { return nome; }
    public void setNome(String nome) { this.nome = nome; }

    public String getTelefone() { return telefone; }
    public void setTelefone(String telefone) { this.telefone = telefone; }

    public String getEndereco() { return endereco; }
    public void setEndereco(String endereco) { this.endereco = endereco; }

    public List<Professor> getProfessores() { return professores; }
    public void setProfessores(List<Professor> professores) { this.professores = professores; }
}
