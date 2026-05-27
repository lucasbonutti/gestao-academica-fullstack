import { Injectable } from '@angular/core';
import { HttpClient } from '@angular/common/http';
import { Observable } from 'rxjs';
import { PageResponse } from '../../../core/models/page-response.model';
import { IesResponse } from '../../../core/models/ies.model';

@Injectable({
  providedIn: 'root'
})
export class IesService {
  private readonly API = 'http://localhost:8080/ies';

  constructor(private http: HttpClient) {}

  listar(page = 0, size = 100): Observable<PageResponse<IesResponse>> {
    return this.http.get<PageResponse<IesResponse>>(
      `${this.API}?page=${page}&size=${size}`
    );
  }

  buscarPorId(id: number): Observable<IesResponse> {
    return this.http.get<IesResponse>(`${this.API}/${id}`);
  }
}