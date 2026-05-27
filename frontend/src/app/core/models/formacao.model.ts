export type Titulacao =
  | 'GRADUACAO'
  | 'ESPECIALIZACAO'
  | 'MBA'
  | 'MESTRADO'
  | 'DOUTORADO'
  | 'POS_DOUTORADO';

export interface FormacaoResponse {
  id: number;
  professorId: number;
  professorNome: string;
  titulacao: Titulacao;
  instituicao: string;
  nomeCurso: string;
  anoConclusao: number;
}

export interface FormacaoCriacaoRequest {
  professorId: number;
  titulacao: Titulacao;
  instituicao: string;
  nomeCurso: string;
  anoConclusao: number;
}

export interface FormacaoAtualizacaoRequest {
  id: number;
  titulacao?: Titulacao;
  instituicao?: string;
  nomeCurso?: string;
  anoConclusao?: number;
}