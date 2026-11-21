package com.vivian.reforco_connect.repository;

import com.vivian.reforco_connect.domain.Professor;
import com.vivian.reforco_connect.dto.RelatorioEngajamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ProfessorRepository extends JpaRepository<Professor, Long> {

    /*
     * Relatório de engajamento: uma linha por professor, com o total de
     * agendamentos e o total de horas.
     *
     * - JOIN instituicao (INNER): professor.instituicao_id é NOT NULL, então
     *   todo professor tem instituição; o INNER JOIN não descarta ninguém.
     * - LEFT JOIN agendamento: mantém o professor mesmo sem nenhum agendamento.
     *   Nesse caso as colunas de "a" vêm NULL, e:
     *     COUNT(a.id)  -> 0   (COUNT de coluna ignora NULL; COUNT(*) daria 1!)
     *     SUM(...)     -> NULL, por isso o COALESCE(..., 0)
     *   Com INNER JOIN esse professor simplesmente sumiria do relatório.
     * - GROUP BY: agrupa as linhas do join por professor para agregar.
     * - Aliases entre aspas: o Postgres converte nomes sem aspas para
     *   minúsculas, e a projeção precisa de "totalAgendamentos".
     */
    @Query(value = """
            SELECT p.id                                                  AS "id",
                   p.nome                                                AS "nome",
                   i.nome                                                AS "instituicao",
                   COUNT(a.id)                                           AS "totalAgendamentos",
                   COALESCE(ROUND(SUM(a.duracao_minutos) / 60.0, 1), 0)  AS "totalHoras"
              FROM professor p
              JOIN instituicao i      ON i.id = p.instituicao_id
              LEFT JOIN agendamento a ON a.professor_id = p.id
             GROUP BY p.id, p.nome, i.nome
             ORDER BY "totalAgendamentos" DESC, p.nome
            """, nativeQuery = true)
    List<RelatorioEngajamento> relatorioEngajamento();
}
