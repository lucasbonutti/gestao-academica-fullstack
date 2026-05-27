export interface IesResponse {
  id: number;
  nome: string;
  endereco: string;
  telefone: string;
  dataCadastro: string;
}

export interface IesCriacaoRequest {
  nome: string;
  endereco: string;
  telefone: string;
}

export interface IesAtualizacaoRequest {
  id: number;
  nome?: string;
  endereco?: string;
  telefone?: string;
}