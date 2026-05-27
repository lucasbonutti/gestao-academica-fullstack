import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageResponse } from '../../../core/models/page-response.model';
import {
  MatrizCurricularCriacaoRequest,
  MatrizCurricularAtualizacaoRequest,
  MatrizCurricularResponse
} from '../../../core/models/matriz-curricular.model';

@Injectable({
  providedIn: 'root'
})
export class MatrizesCurricularesService {
  private readonly API = 'http://localhost:8080/matrizes-curriculares';

  constructor(private http: HttpClient) {}

  listar(page = 0, size = 10): Observable<PageResponse<MatrizCurricularResponse>> {
    return this.http.get<PageResponse<MatrizCurricularResponse>>(
      `${this.API}?page=${page}&size=${size}`
    );
  }

  listarAtivas(page = 0, size = 10): Observable<PageResponse<MatrizCurricularResponse>> {
    return this.http.get<PageResponse<MatrizCurricularResponse>>(
      `${this.API}/ativos?page=${page}&size=${size}`
    );
  }

  listarInativas(page = 0, size = 10): Observable<PageResponse<MatrizCurricularResponse>> {
    return this.http.get<PageResponse<MatrizCurricularResponse>>(
      `${this.API}/inativos?page=${page}&size=${size}`
    );
  }

  listarPorCurso(cursoId: number, page = 0, size = 10): Observable<PageResponse<MatrizCurricularResponse>> {
    return this.http.get<PageResponse<MatrizCurricularResponse>>(
      `${this.API}/curso/${cursoId}?page=${page}&size=${size}`
    );
  }

  buscarPorId(id: number): Observable<MatrizCurricularResponse> {
    return this.http.get<MatrizCurricularResponse>(`${this.API}/${id}`);
  }

  criar(data: MatrizCurricularCriacaoRequest): Observable<MatrizCurricularResponse> {
    return this.http.post<MatrizCurricularResponse>(this.API, data);
  }

  atualizar(data: MatrizCurricularAtualizacaoRequest): Observable<MatrizCurricularResponse> {
    return this.http.put<MatrizCurricularResponse>(this.API, data);
  }

  ativar(id: number): Observable<void> {
    return this.http.patch<void>(`${this.API}/${id}/ativar`, {});
  }

  inativar(id: number): Observable<void> {
    return this.http.patch<void>(`${this.API}/${id}/inativar`, {});
  }
}