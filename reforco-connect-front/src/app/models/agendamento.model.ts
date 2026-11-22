import { Professor } from './professor.model';

export interface Agendamento {
  id?: number;
  alunoNome: string;
  // LocalDateTime no back: "2026-11-25T14:30:00"
  dataHora: string;
  duracaoMinutos: number;
  status: string;
  // Na resposta vem o professor completo; no envio basta { id }.
  professor: Professor | { id: number };
}

export const STATUS_AGENDAMENTO = ['Agendado', 'Confirmado', 'Concluído', 'Cancelado'];
