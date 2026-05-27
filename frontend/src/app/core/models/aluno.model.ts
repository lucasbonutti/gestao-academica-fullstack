export interface AlunoResponse {
  id: number;
  matricula: string;
  nomeCompleto: string;
  dataCadastro: string;
  ativo: boolean;
}

export interface AlunoCriacaoRequest {
  matricula: string;
  nomeCompleto: string;
}

export interface AlunoAtualizacaoRequest {
  id: number;
  matricula?: string;
  nomeCompleto?: string;
}