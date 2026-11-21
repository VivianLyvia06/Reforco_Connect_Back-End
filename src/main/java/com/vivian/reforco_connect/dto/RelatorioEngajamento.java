package com.vivian.reforco_connect.dto;

/**
 * Projeção (interface) do Spring Data para o relatório de engajamento.
 *
 * Cada getter corresponde a um alias da native query em
 * ProfessorRepository.relatorioEngajamento(). O Spring cria a implementação
 * em tempo de execução. Assim o endpoint devolve só estas colunas, e não a
 * entidade Professor inteira.
 */
public interface RelatorioEngajamento {

    Long getId();

    String getNome();

    String getInstituicao();

    Long getTotalAgendamentos();

    Double getTotalHoras();
}
