import { IesResponse } from './ies.model';

export interface EscolaResponse {
  id: number;
  nome: string;
  coordenador: string;
  ies: IesResponse;
  dataCadastro: string;
  ativo: boolean;
}

export interface EscolaCriacaoRequest {
  nome: string;
  coordenador: string;
  iesId: number;
}

export interface EscolaAtualizacaoRequest {
  id: number;
  nome?: string;
  coordenador?: string;
  iesId?: number;
}