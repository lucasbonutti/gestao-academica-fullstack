import { Injectable } from '@angular/core';
import { MatrizSlot, MatrizVisualState } from '../../core/models/matriz-visual.model';

@Injectable({
  providedIn: 'root'
})
export class MatrizVisualService {
  private readonly STORAGE_KEY = 'monitoria_matriz_visual';

  private readonly dias: Array<'Segunda' | 'Terça' | 'Quarta' | 'Quinta' | 'Sexta'> = [
    'Segunda',
    'Terça',
    'Quarta',
    'Quinta',
    'Sexta'
  ];

  private readonly horarios = ['08:00', '10:00', '14:00', '16:00', '19:00'];

  criarEstadoInicial(): MatrizVisualState {
    const slots: MatrizSlot[] = [];

    for (const horario of this.horarios) {
      for (const dia of this.dias) {
        slots.push({
          dia,
          horario,
          disciplina: null
        });
      }
    }

    return { slots };
  }

  carregar(): MatrizVisualState {
    const salvo = localStorage.getItem(this.STORAGE_KEY);

    if (!salvo) {
      return this.criarEstadoInicial();
    }

    try {
      const parsed = JSON.parse(salvo) as MatrizVisualState;

      if (!parsed?.slots?.length) {
        return this.criarEstadoInicial();
      }

      return parsed;
    } catch {
      return this.criarEstadoInicial();
    }
  }

  salvar(state: MatrizVisualState): void {
    localStorage.setItem(this.STORAGE_KEY, JSON.stringify(state));
  }

  limpar(): void {
    localStorage.removeItem(this.STORAGE_KEY);
  }
}