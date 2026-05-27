import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageResponse } from '../../../core/models/page-response.model';
import { AlunoCriacaoRequest, AlunoResponse, AlunoAtualizacaoRequest } from '../../../core/models/aluno.model';

@Injectable({
  providedIn: 'root'
})
export class AlunosService {
  private readonly API = 'http://localhost:8080/alunos';

  constructor(private http: HttpClient) {}

  listar(page = 0, size = 10): Observable<PageResponse<AlunoResponse>> {
    return this.http.get<PageResponse<AlunoResponse>>(`${this.API}?page=${page}&size=${size}`);
  }

  listarAtivos(page = 0, size = 10): Observable<PageResponse<AlunoResponse>> {
    return this.http.get<PageResponse<AlunoResponse>>(`${this.API}/ativos?page=${page}&size=${size}`);
  }

  listarInativos(page = 0, size = 10): Observable<PageResponse<AlunoResponse>> {
    return this.http.get<PageResponse<AlunoResponse>>(`${this.API}/inativos?page=${page}&size=${size}`);
  }

  buscarPorId(id: number): Observable<AlunoResponse> {
    return this.http.get<AlunoResponse>(`${this.API}/${id}`);
  }

  criar(data: AlunoCriacaoRequest): Observable<AlunoResponse> {
    return this.http.post<AlunoResponse>(this.API, data);
  }

  atualizar(data: AlunoAtualizacaoRequest): Observable<AlunoResponse> {
    return this.http.put<AlunoResponse>(this.API, data);
  }

  ativar(id: number): Observable<void> {
    return this.http.patch<void>(`${this.API}/${id}/ativar`, {});
  }

  inativar(id: number): Observable<void> {
    return this.http.patch<void>(`${this.API}/${id}/inativar`, {});
  }
}