import { EscolaResponse } from './escola.model';

export type TurnoCurso = 'MATUTINO' | 'VESPERTINO' | 'NOTURNO' | 'INTEGRAL';

export interface CursoResponse {
  id: number;
  sigla: string;
  descricao: string;
  escola: EscolaResponse;
  turno: TurnoCurso;
  coordenador: string;
  dataCadastro: string;
  ativo: boolean;
}

export interface CursoCriacaoRequest {
  sigla: string;
  descricao: string;
  escolaId: number;
  turno: TurnoCurso;
  coordenador: string;
}

export interface CursoAtualizacaoRequest {
  id: number;
  sigla?: string;
  descricao?: string;
  escolaId?: number;
  turno?: TurnoCurso;
  coordenador?: string;
}