import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageResponse } from '../../../core/models/page-response.model';
import { IesAtualizacaoRequest, IesCriacaoRequest, IesResponse } from '../../../core/models/ies.model';

@Injectable({
  providedIn: 'root'
})
export class IesService {
  private readonly API = 'http://localhost:8080/ies';

  constructor(private http: HttpClient) {}

  listar(page = 0, size = 10): Observable<PageResponse<IesResponse>> {
    return this.http.get<PageResponse<IesResponse>>(`${this.API}?page=${page}&size=${size}`);
  }

  buscarPorId(id: number): Observable<IesResponse> {
    return this.http.get<IesResponse>(`${this.API}/${id}`);
  }

  criar(data: IesCriacaoRequest): Observable<IesResponse> {
    return this.http.post<IesResponse>(this.API, data);
  }

  atualizar(data: IesAtualizacaoRequest): Observable<IesResponse> {
    return this.http.put<IesResponse>(this.API, data);
  }

  excluir(id: number): Observable<void> {
    return this.http.delete<void>(`${this.API}/${id}`);
  }
}