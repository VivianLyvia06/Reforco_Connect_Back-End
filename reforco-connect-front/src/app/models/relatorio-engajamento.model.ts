// Uma linha de GET /professores/relatorio-engajamento
// (projeção da native query com LEFT JOIN + COUNT/SUM + GROUP BY).
export interface RelatorioEngajamento {
  id: number;
  nome: string;
  instituicao: string;
  totalAgendamentos: number;
  totalHoras: number;
}
