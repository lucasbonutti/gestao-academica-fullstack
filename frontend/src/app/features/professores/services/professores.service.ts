import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageResponse } from '../../../core/models/page-response.model';
import {
  ProfessorAtualizacaoRequest,
  ProfessorCriacaoRequest,
  ProfessorResponse
} from '../../../core/models/professor.model';

@Injectable({
  providedIn: 'root'
})
export class ProfessoresService {
  private readonly API = 'http://localhost:8080/professores';

  constructor(private http: HttpClient) {}

  listar(page = 0, size = 10): Observable<PageResponse<ProfessorResponse>> {
    return this.http.get<PageResponse<ProfessorResponse>>(
      `${this.API}?page=${page}&size=${size}`
    );
  }

  listarAtivos(page = 0, size = 10): Observable<PageResponse<ProfessorResponse>> {
    return this.http.get<PageResponse<ProfessorResponse>>(
      `${this.API}/ativos?page=${page}&size=${size}`
    );
  }

  listarInativos(page = 0, size = 10): Observable<PageResponse<ProfessorResponse>> {
    return this.http.get<PageResponse<ProfessorResponse>>(
      `${this.API}/inativos?page=${page}&size=${size}`
    );
  }

  listarPorEscola(escolaId: number, page = 0, size = 10): Observable<PageResponse<ProfessorResponse>> {
    return this.http.get<PageResponse<ProfessorResponse>>(
      `${this.API}/escola/${escolaId}?page=${page}&size=${size}`
    );
  }

  buscarPorId(id: number): Observable<ProfessorResponse> {
    return this.http.get<ProfessorResponse>(`${this.API}/${id}`);
  }

  criar(data: ProfessorCriacaoRequest): Observable<ProfessorResponse> {
    return this.http.post<ProfessorResponse>(this.API, data);
  }

  atualizar(data: ProfessorAtualizacaoRequest): Observable<ProfessorResponse> {
    return this.http.put<ProfessorResponse>(this.API, data);
  }

  ativar(id: number): Observable<void> {
    return this.http.patch<void>(`${this.API}/${id}/ativar`, {});
  }

  inativar(id: number): Observable<void> {
    return this.http.patch<void>(`${this.API}/${id}/inativar`, {});
  }
}