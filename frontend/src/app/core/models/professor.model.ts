import { EscolaResponse } from './escola.model';

export interface ProfessorResponse {
  id: number;
  matricula: string;
  nomeCompleto: string;
  email: string;
  telefone: string;
  escola: EscolaResponse;
  dataCadastro: string;
  ativo: boolean;
}

export interface ProfessorCriacaoRequest {
  matricula: string;
  nomeCompleto: string;
  email: string;
  telefone: string;
  escolaId: number;
}

export interface ProfessorAtualizacaoRequest {
  id: number;
  matricula?: string;
  nomeCompleto?: string;
  email?: string;
  telefone?: string;
  escolaId?: number;
}