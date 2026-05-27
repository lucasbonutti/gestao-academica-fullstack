import { CursoResponse } from './curso.model';

export interface MatrizCurricularResponse {
  id: number;
  nome: string;
  descricao: string;
  curso: CursoResponse;
  dataCadastro: string;
  ativo: boolean;
}

export interface MatrizCurricularCriacaoRequest {
  nome: string;
  descricao: string;
  cursoId: number;
}

export interface MatrizCurricularAtualizacaoRequest {
  id: number;
  nome?: string;
  descricao?: string;
  cursoId?: number;
}