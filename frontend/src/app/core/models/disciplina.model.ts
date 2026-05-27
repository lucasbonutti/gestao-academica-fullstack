import { EscolaResponse } from './escola.model';

export interface DisciplinaResponse {
  id: number;
  sigla: string;
  descricao: string;
  cargaHoraria: number;
  escola: EscolaResponse;
  dataCadastro: string;
  ativo: boolean;
}

export interface DisciplinaCriacaoRequest {
  sigla: string;
  descricao: string;
  cargaHoraria: number;
  escolaId: number;
}

export interface DisciplinaAtualizacaoRequest {
  id: number;
  sigla?: string;
  descricao?: string;
  cargaHoraria?: number;
  escolaId?: number;
}

export interface DisciplinaMatrizCard {
  id: number;
  sigla: string;
  descricao: string;
}