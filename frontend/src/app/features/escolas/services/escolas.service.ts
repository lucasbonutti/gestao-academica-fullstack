import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageResponse } from '../../../core/models/page-response.model';
import {
  EscolaAtualizacaoRequest,
  EscolaCriacaoRequest,
  EscolaResponse
} from '../../../core/models/escola.model';

@Injectable({
  providedIn: 'root'
})
export class EscolasService {
  private readonly API = 'http://localhost:8080/escolas';

  constructor(private http: HttpClient) {}

  listar(page = 0, size = 10): Observable<PageResponse<EscolaResponse>> {
    return this.http.get<PageResponse<EscolaResponse>>(
      `${this.API}?page=${page}&size=${size}`
    );
  }

  listarAtivas(page = 0, size = 10): Observable<PageResponse<EscolaResponse>> {
    return this.http.get<PageResponse<EscolaResponse>>(
      `${this.API}/ativos?page=${page}&size=${size}`
    );
  }

  listarInativas(page = 0, size = 10): Observable<PageResponse<EscolaResponse>> {
    return this.http.get<PageResponse<EscolaResponse>>(
      `${this.API}/inativos?page=${page}&size=${size}`
    );
  }

  buscarPorId(id: number): Observable<EscolaResponse> {
    return this.http.get<EscolaResponse>(`${this.API}/${id}`);
  }

  criar(data: EscolaCriacaoRequest): Observable<EscolaResponse> {
    return this.http.post<EscolaResponse>(this.API, data);
  }

  atualizar(data: EscolaAtualizacaoRequest): Observable<EscolaResponse> {
    return this.http.put<EscolaResponse>(this.API, data);
  }

  ativar(id: number): Observable<void> {
    return this.http.patch<void>(`${this.API}/${id}/ativar`, {});
  }

  inativar(id: number): Observable<void> {
    return this.http.patch<void>(`${this.API}/${id}/inativar`, {});
  }
}