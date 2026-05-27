import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageResponse } from '../../../core/models/page-response.model';
import {
  DisciplinaAtualizacaoRequest,
  DisciplinaCriacaoRequest,
  DisciplinaResponse
} from '../../../core/models/disciplina.model';

@Injectable({
  providedIn: 'root'
})
export class DisciplinasService {
  private readonly API = 'http://localhost:8080/disciplinas';

  constructor(private http: HttpClient) {}

  listar(page = 0, size = 10): Observable<PageResponse<DisciplinaResponse>> {
    return this.http.get<PageResponse<DisciplinaResponse>>(
      `${this.API}?page=${page}&size=${size}`
    );
  }

  listarAtivas(page = 0, size = 10): Observable<PageResponse<DisciplinaResponse>> {
    return this.http.get<PageResponse<DisciplinaResponse>>(
      `${this.API}/ativos?page=${page}&size=${size}`
    );
  }

  listarInativas(page = 0, size = 10): Observable<PageResponse<DisciplinaResponse>> {
    return this.http.get<PageResponse<DisciplinaResponse>>(
      `${this.API}/inativos?page=${page}&size=${size}`
    );
  }

  listarPorEscola(escolaId: number, page = 0, size = 10): Observable<PageResponse<DisciplinaResponse>> {
    return this.http.get<PageResponse<DisciplinaResponse>>(
      `${this.API}/escola/${escolaId}?page=${page}&size=${size}`
    );
  }

  buscarPorId(id: number): Observable<DisciplinaResponse> {
    return this.http.get<DisciplinaResponse>(`${this.API}/${id}`);
  }

  criar(data: DisciplinaCriacaoRequest): Observable<DisciplinaResponse> {
    return this.http.post<DisciplinaResponse>(this.API, data);
  }

  atualizar(data: DisciplinaAtualizacaoRequest): Observable<DisciplinaResponse> {
    return this.http.put<DisciplinaResponse>(this.API, data);
  }

  ativar(id: number): Observable<void> {
    return this.http.patch<void>(`${this.API}/${id}/ativar`, {});
  }

  inativar(id: number): Observable<void> {
    return this.http.patch<void>(`${this.API}/${id}/inativar`, {});
  }
}