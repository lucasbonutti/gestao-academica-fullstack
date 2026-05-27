import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageResponse } from '../../../core/models/page-response.model';
import {
  MonitoriaAtualizacaoRequest,
  MonitoriaCriacaoRequest,
  MonitoriaResponse
} from '../../../core/models/monitoria.model';

@Injectable({
  providedIn: 'root'
})
export class MonitoriasService {
  private readonly API = 'http://localhost:8080/monitorias';

  constructor(private http: HttpClient) {}

  listar(page = 0, size = 10): Observable<PageResponse<MonitoriaResponse>> {
    return this.http.get<PageResponse<MonitoriaResponse>>(
      `${this.API}?page=${page}&size=${size}`
    );
  }

  listarPorProfessor(professorId: number, page = 0, size = 10): Observable<PageResponse<MonitoriaResponse>> {
    return this.http.get<PageResponse<MonitoriaResponse>>(
      `${this.API}/professor/${professorId}?page=${page}&size=${size}`
    );
  }

  listarPorAluno(alunoId: number, page = 0, size = 10): Observable<PageResponse<MonitoriaResponse>> {
    return this.http.get<PageResponse<MonitoriaResponse>>(
      `${this.API}/aluno/${alunoId}?page=${page}&size=${size}`
    );
  }

  listarPorStatus(status: string, page = 0, size = 10): Observable<PageResponse<MonitoriaResponse>> {
    return this.http.get<PageResponse<MonitoriaResponse>>(
      `${this.API}/status/${status}?page=${page}&size=${size}`
    );
  }

  buscarPorId(id: number): Observable<MonitoriaResponse> {
    return this.http.get<MonitoriaResponse>(`${this.API}/${id}`);
  }

  criar(data: MonitoriaCriacaoRequest): Observable<MonitoriaResponse> {
    return this.http.post<MonitoriaResponse>(this.API, data);
  }

  atualizar(data: MonitoriaAtualizacaoRequest): Observable<MonitoriaResponse> {
    return this.http.put<MonitoriaResponse>(this.API, data);
  }

  finalizar(id: number): Observable<void> {
    return this.http.patch<void>(`${this.API}/${id}/finalizar`, {});
  }
}