import { HttpErrorResponse } from '@angular/common/http';
import { API_URL } from '../api.config';

// 8 -> "08" (números das estatísticas, como no Figma)
export function doisDigitos(valor: number): string {
  return valor < 10 ? '0' + valor : String(valor);
}

// 13.5 -> "13,5"   18 -> "18"
export function formatarHoras(horas: number): string {
  return horas.toLocaleString('pt-BR', { maximumFractionDigits: 1 });
}

// "2026-11-25T14:30:00" -> "25/11/2026 · 14:30"
export function formatarDataHora(dataHora: string): string {
  const [data, hora] = dataHora.split('T');
  const [ano, mes, dia] = data.split('-');
  return `${dia}/${mes}/${ano} · ${hora.substring(0, 5)}`;
}

// Transforma a resposta de erro da API (GlobalExceptionHandler) em texto.
export function mensagemDeErro(erro: HttpErrorResponse): string {
  if (erro.status === 0) {
    return `Não foi possível conectar à API em ${API_URL}. O back-end está rodando?`;
  }
  const corpo = erro.error;
  if (corpo && corpo.campos) {
    return Object.values(corpo.campos).join(' · ');
  }
  if (corpo && corpo.mensagem) {
    return corpo.mensagem;
  }
  return `Erro ${erro.status} ao comunicar com a API.`;
}
