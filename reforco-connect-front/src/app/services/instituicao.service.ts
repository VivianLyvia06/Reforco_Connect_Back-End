import { HttpClient } from '@angular/common/http';
import { Injectable, inject } from '@angular/core';
import { Observable } from 'rxjs';
import { API_URL } from '../api.config';
import { Instituicao } from '../models/instituicao.model';

@Injectable({ providedIn: 'root' })
export class InstituicaoService {
  private http = inject(HttpClient);
  private url = `${API_URL}/instituicoes`;

  listar(): Observable<Instituicao[]> {
    return this.http.get<Instituicao[]>(this.url);
  }

  buscarPorId(id: number): Observable<Instituicao> {
    return this.http.get<Instituicao>(`${this.url}/${id}`);
  }

  cadastrar(instituicao: Instituicao): Observable<Instituicao> {
    return this.http.post<Instituicao>(this.url, instituicao);
  }

  atualizar(id: number, instituicao: Instituicao): Observable<Instituicao> {
    return this.http.put<Instituicao>(`${this.url}/${id}`, instituicao);
  }

  remover(id: number): Observable<void> {
    return this.http.delete<void>(`${this.url}/${id}`);
  }
}
