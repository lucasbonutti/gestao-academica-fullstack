import { AlunoResponse } from './aluno.model';
import { DisciplinaResponse } from './disciplina.model';
import { ProfessorResponse } from './professor.model';

export type TipoMonitoria = 'PRESENCIAL' | 'REMOTO';
export type StatusMonitoria = 'EM_ANDAMENTO' | 'FINALIZADA' | 'CANCELADA';

export interface MonitoriaResponse {
  id: number;
  aluno: AlunoResponse;
  disciplina: DisciplinaResponse;
  professor: ProfessorResponse;
  semestre: string;
  tipoMonitoria: TipoMonitoria;
  local: string;
  dataInicio: string;
  dataFim: string;
  dataCadastro: string;
  status: StatusMonitoria;
}

export interface MonitoriaCriacaoRequest {
  alunoId: number;
  disciplinaId: number;
  professorId: number;
  semestre: string;
  tipoMonitoria: TipoMonitoria;
  local: string;
  dataInicio: string;
  dataFim: string;
}

export interface MonitoriaAtualizacaoRequest {
  id: number;
  alunoId?: number;
  disciplinaId?: number;
  professorId?: number;
  semestre?: string;
  tipoMonitoria?: TipoMonitoria;
  local?: string;
  dataInicio?: string;
  dataFim?: string;
}