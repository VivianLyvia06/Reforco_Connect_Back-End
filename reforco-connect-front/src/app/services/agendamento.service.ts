import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL } from '../api.config';
import { Agendamento } from '../models/agendamento.model';

@Injectable({ providedIn: 'root' })
export class AgendamentoService {
  private http = inject(HttpClient);
  private url = `${API_URL}/agendamentos`;

  listar(): Observable<Agendamento[]> {
    return this.http.get<Agendamento[]>(this.url);
  }

  buscarPorId(id: number): Observable<Agendamento> {
    return this.http.get<Agendamento>(`${this.url}/${id}`);
  }

  cadastrar(agendamento: Agendamento): Observable<Agendamento> {
    return this.http.post<Agendamento>(this.url, this.comSegundos(agendamento));
  }

  atualizar(id: number, agendamento: Agendamento): Observable<Agendamento> {
    return this.http.put<Agendamento>(`${this.url}/${id}`, this.comSegundos(agendamento));
  }

  remover(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }

  // O <input type="datetime-local"> devolve "2026-11-25T14:30" (sem segundos).
  // O LocalDateTime do Spring espera o ISO completo "2026-11-25T14:30:00".
  private comSegundos(agendamento: Agendamento): Agendamento {
    let dataHora = agendamento.dataHora;
    if (dataHora && dataHora.length === 16) {
      dataHora = dataHora + ':00';
    }
    return { ...agendamento, dataHora };
  }
}
