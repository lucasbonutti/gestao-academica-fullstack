import { DisciplinaResponse } from './disciplina.model';
import { MatrizCurricularResponse } from './matriz-curricular.model';

export interface MatrizDisciplinaResponse {
  id: number;
  matriz: MatrizCurricularResponse;
  disciplina: DisciplinaResponse;
  preRequisitos: DisciplinaResponse[];
}

export interface MatrizDisciplinaCriacaoRequest {
  matrizId: number;
  disciplinaId: number;
  preRequisitosIds?: number[];
}

export interface MatrizDisciplinaAtualizacaoRequest {
  id: number;
  matrizId?: number;
  disciplinaId?: number;
  preRequisitosIds?: number[];
}