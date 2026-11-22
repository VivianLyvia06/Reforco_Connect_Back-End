import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL } from '../api.config';
import { Professor } from '../models/professor.model';
import { RelatorioEngajamento } from '../models/relatorio-engajamento.model';

@Injectable({ providedIn: 'root' })
export class ProfessorService {
  private http = inject(HttpClient);
  private url = `${API_URL}/professores`;

  listar(): Observable<Professor[]> {
    return this.http.get<Professor[]>(this.url);
  }

  buscarPorId(id: number): Observable<Professor> {
    return this.http.get<Professor>(`${this.url}/${id}`);
  }

  cadastrar(professor: Professor): Observable<Professor> {
    return this.http.post<Professor>(this.url, professor);
  }

  atualizar(id: number, professor: Professor): Observable<Professor> {
    return this.http.put<Professor>(`${this.url}/${id}`, professor);
  }

  remover(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }

  relatorioEngajamento(): Observable<RelatorioEngajamento[]> {
    return this.http.get<RelatorioEngajamento[]>(`${this.url}/relatorio-engajamento`);
  }
}
