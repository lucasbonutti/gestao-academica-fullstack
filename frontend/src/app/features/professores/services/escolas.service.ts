import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageResponse } from '../../../core/models/page-response.model';
import { EscolaResponse } from '../../../core/models/escola.model';

@Injectable({
  providedIn: 'root'
})
export class EscolasService {
  private readonly API = 'http://localhost:8080/escolas';

  constructor(private http: HttpClient) {}

  listar(page = 0, size = 100): Observable<PageResponse<EscolaResponse>> {
    return this.http.get<PageResponse<EscolaResponse>>(
      `${this.API}?page=${page}&size=${size}`
    );
  }

  listarAtivas(page = 0, size = 100): Observable<PageResponse<EscolaResponse>> {
    return this.http.get<PageResponse<EscolaResponse>>(
      `${this.API}/ativos?page=${page}&size=${size}`
    );
  }
}