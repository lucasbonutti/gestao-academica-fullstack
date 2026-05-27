import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageResponse } from '../../../core/models/page-response.model';
import {
  CursoAtualizacaoRequest,
  CursoCriacaoRequest,
  CursoResponse
} from '../../../core/models/curso.model';

@Injectable({
  providedIn: 'root'
})
export class CursosService {
  private readonly API = 'http://localhost:8080/cursos';

  constructor(private http: HttpClient) {}

  listar(page = 0, size = 10): Observable<PageResponse<CursoResponse>> {
    return this.http.get<PageResponse<CursoResponse>>(`${this.API}?page=${page}&size=${size}`);
  }

  listarAtivos(page = 0, size = 10): Observable<PageResponse<CursoResponse>> {
    return this.http.get<PageResponse<CursoResponse>>(`${this.API}/ativos?page=${page}&size=${size}`);
  }

  listarInativos(page = 0, size = 10): Observable<PageResponse<CursoResponse>> {
    return this.http.get<PageResponse<CursoResponse>>(`${this.API}/inativos?page=${page}&size=${size}`);
  }

  listarPorEscola(escolaId: number, page = 0, size = 10): Observable<PageResponse<CursoResponse>> {
    return this.http.get<PageResponse<CursoResponse>>(`${this.API}/escola/${escolaId}?page=${page}&size=${size}`);
  }

  buscarPorId(id: number): Observable<CursoResponse> {
    return this.http.get<CursoResponse>(`${this.API}/${id}`);
  }

  criar(data: CursoCriacaoRequest): Observable<CursoResponse> {
    return this.http.post<CursoResponse>(this.API, data);
  }

  atualizar(data: CursoAtualizacaoRequest): Observable<CursoResponse> {
    return this.http.put<CursoResponse>(this.API, data);
  }

  ativar(id: number): Observable<void> {
    return this.http.patch<void>(`${this.API}/${id}/ativar`, {});
  }

  inativar(id: number): Observable<void> {
    return this.http.patch<void>(`${this.API}/${id}/inativar`, {});
  }
}