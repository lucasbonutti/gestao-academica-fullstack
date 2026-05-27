export type DiaSemana = 'Segunda' | 'Terça' | 'Quarta' | 'Quinta' | 'Sexta';

export interface DisciplinaVisual {
  id: number;
  sigla: string;
  descricao: string;
}

export interface MatrizSlot {
  dia: DiaSemana;
  horario: string;
  disciplina: DisciplinaVisual | null;
}

export interface MatrizVisualState {
  slots: MatrizSlot[];
}