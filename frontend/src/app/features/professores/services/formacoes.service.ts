import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageResponse } from '../../../core/models/page-response.model';
import {
  FormacaoAtualizacaoRequest,
  FormacaoCriacaoRequest,
  FormacaoResponse
} from '../../../core/models/formacao.model';

@Injectable({
  providedIn: 'root'
})
export class FormacoesService {
  private readonly API = 'http://localhost:8080/formacoes';

  constructor(private http: HttpClient) {}

  listar(page = 0, size = 20): Observable<PageResponse<FormacaoResponse>> {
    return this.http.get<PageResponse<FormacaoResponse>>(`${this.API}?page=${page}&size=${size}`);
  }

  listarPorProfessor(professorId: number, page = 0, size = 20): Observable<PageResponse<FormacaoResponse>> {
    return this.http.get<PageResponse<FormacaoResponse>>(
      `${this.API}/professor/${professorId}?page=${page}&size=${size}`
    );
  }

  buscarPorId(id: number): Observable<FormacaoResponse> {
    return this.http.get<FormacaoResponse>(`${this.API}/${id}`);
  }

  criar(data: FormacaoCriacaoRequest): Observable<FormacaoResponse> {
    return this.http.post<FormacaoResponse>(this.API, data);
  }

  atualizar(data: FormacaoAtualizacaoRequest): Observable<FormacaoResponse> {
    return this.http.put<FormacaoResponse>(this.API, data);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API}/${id}`);
  }
}