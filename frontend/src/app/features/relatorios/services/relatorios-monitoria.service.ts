import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageResponse } from '../../../core/models/page-response.model';
import {
  RelatorioMonitoriaCriacaoRequest,
  RelatorioMonitoriaResponse
} from '../../../core/models/relatorio-monitoria.model';

@Injectable({
  providedIn: 'root'
})
export class RelatoriosMonitoriaService {
  private readonly API = 'http://localhost:8080/relatorios-monitoria';

  constructor(private http: HttpClient) {}

  listar(page = 0, size = 10): Observable<PageResponse<RelatorioMonitoriaResponse>> {
    return this.http.get<PageResponse<RelatorioMonitoriaResponse>>(`${this.API}?page=${page}&size=${size}`);
  }

  listarPorProfessor(professorId: number, page = 0, size = 10): Observable<PageResponse<RelatorioMonitoriaResponse>> {
    return this.http.get<PageResponse<RelatorioMonitoriaResponse>>(`${this.API}/professor/${professorId}?page=${page}&size=${size}`);
  }

  listarPorDisciplina(disciplinaId: number, page = 0, size = 10): Observable<PageResponse<RelatorioMonitoriaResponse>> {
    return this.http.get<PageResponse<RelatorioMonitoriaResponse>>(`${this.API}/disciplina/${disciplinaId}?page=${page}&size=${size}`);
  }

  listarPorAluno(alunoId: number, page = 0, size = 10): Observable<PageResponse<RelatorioMonitoriaResponse>> {
    return this.http.get<PageResponse<RelatorioMonitoriaResponse>>(`${this.API}/aluno/${alunoId}?page=${page}&size=${size}`);
  }

  listarPorSemestre(semestre: string, page = 0, size = 10): Observable<PageResponse<RelatorioMonitoriaResponse>> {
    return this.http.get<PageResponse<RelatorioMonitoriaResponse>>(`${this.API}/semestre/${semestre}?page=${page}&size=${size}`);
  }

  listarPorStatus(status: string, page = 0, size = 10): Observable<PageResponse<RelatorioMonitoriaResponse>> {
    return this.http.get<PageResponse<RelatorioMonitoriaResponse>>(`${this.API}/status/${status}?page=${page}&size=${size}`);
  }

  buscarPorId(id: number): Observable<RelatorioMonitoriaResponse> {
    return this.http.get<RelatorioMonitoriaResponse>(`${this.API}/${id}`);
  }

  buscarPorMonitoria(monitoriaId: number): Observable<RelatorioMonitoriaResponse> {
    return this.http.get<RelatorioMonitoriaResponse>(`${this.API}/monitoria/${monitoriaId}`);
  }

  criar(data: RelatorioMonitoriaCriacaoRequest): Observable<RelatorioMonitoriaResponse> {
    return this.http.post<RelatorioMonitoriaResponse>(this.API, data);
  }
}