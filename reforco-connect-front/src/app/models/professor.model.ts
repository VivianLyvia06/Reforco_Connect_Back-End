import { Instituicao } from './instituicao.model';

export interface Professor {
  id?: number;
  nome: string;
  disciplina: string;
  fotoUrl: string | null;
  // Na resposta vem a instituição completa; no envio basta { id }.
  instituicao: Instituicao | { id: number };
}
