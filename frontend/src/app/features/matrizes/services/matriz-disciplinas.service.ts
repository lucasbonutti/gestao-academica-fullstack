import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageResponse } from '../../../core/models/page-response.model';
import {
  MatrizDisciplinaAtualizacaoRequest,
  MatrizDisciplinaCriacaoRequest,
  MatrizDisciplinaResponse
} from '../../../core/models/matriz-disciplina.model';

@Injectable({
  providedIn: 'root'
})
export class MatrizDisciplinasService {
  private readonly API = 'http://localhost:8080/matriz-disciplinas';

  constructor(private http: HttpClient) {}

  listar(page = 0, size = 20): Observable<PageResponse<MatrizDisciplinaResponse>> {
    return this.http.get<PageResponse<MatrizDisciplinaResponse>>(`${this.API}?page=${page}&size=${size}`);
  }

  listarPorMatriz(matrizId: number, page = 0, size = 20): Observable<PageResponse<MatrizDisciplinaResponse>> {
    return this.http.get<PageResponse<MatrizDisciplinaResponse>>(`${this.API}/matriz/${matrizId}?page=${page}&size=${size}`);
  }

  listarPorDisciplina(disciplinaId: number, page = 0, size = 20): Observable<PageResponse<MatrizDisciplinaResponse>> {
    return this.http.get<PageResponse<MatrizDisciplinaResponse>>(`${this.API}/disciplina/${disciplinaId}?page=${page}&size=${size}`);
  }

  buscarPorId(id: number): Observable<MatrizDisciplinaResponse> {
    return this.http.get<MatrizDisciplinaResponse>(`${this.API}/${id}`);
  }

  criar(data: MatrizDisciplinaCriacaoRequest): Observable<MatrizDisciplinaResponse> {
    return this.http.post<MatrizDisciplinaResponse>(this.API, data);
  }

  atualizar(data: MatrizDisciplinaAtualizacaoRequest): Observable<MatrizDisciplinaResponse> {
    return this.http.put<MatrizDisciplinaResponse>(this.API, data);
  }
}