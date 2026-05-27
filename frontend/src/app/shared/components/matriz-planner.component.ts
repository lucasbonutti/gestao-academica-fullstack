import { Component, Input, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import {
  DiaSemana,
  DisciplinaVisual,
  MatrizSlot,
  MatrizVisualState
} from '../../core/models/matriz-visual.model';
import { MatrizVisualService } from '../services/matriz-visual.service';

@Component({
  selector: 'app-matriz-planner',
  standalone: true,
  imports: [CommonModule],
  templateUrl: './matriz-planner.component.html',
  styleUrl: './matriz-planner.component.css'
})
export class MatrizPlannerComponent implements OnInit {
  @Input() disciplinasDisponiveis: DisciplinaVisual[] = [];
  @Input() mostrarAcoes = true;
  @Input() titulo = 'Matriz ofertada';
  @Input() subtitulo = 'Distribua visualmente as disciplinas disponíveis em uma grade semanal simples.';

  readonly dias: DiaSemana[] = ['Segunda', 'Terça', 'Quarta', 'Quinta', 'Sexta'];
  readonly horarios = ['08:00', '10:00', '14:00', '16:00', '19:00'];

  estadoSalvo!: MatrizVisualState;
  estadoEditavel!: MatrizVisualState;

  disciplinaSelecionadaId: number | null = null;
  successMessage = '';

  constructor(private matrizVisualService: MatrizVisualService) {}

  ngOnInit(): void {
    this.estadoSalvo = this.clonarEstado(this.matrizVisualService.carregar());
    this.estadoEditavel = this.clonarEstado(this.estadoSalvo);
  }

  get disciplinasDisponiveisParaExibir(): DisciplinaVisual[] {
    return this.disciplinasDisponiveis;
  }

  selecionarDisciplina(id: number): void {
    this.disciplinaSelecionadaId = this.disciplinaSelecionadaId === id ? null : id;
  }

  slotPor(dia: DiaSemana, horario: string): MatrizSlot | undefined {
    return this.estadoEditavel.slots.find(
      (slot) => slot.dia === dia && slot.horario === horario
    );
  }

  adicionarOuTrocar(dia: DiaSemana, horario: string): void {
    if (!this.disciplinaSelecionadaId) {
      return;
    }

    const disciplina = this.disciplinasDisponiveis.find(
      (item) => item.id === this.disciplinaSelecionadaId
    );

    if (!disciplina) {
      return;
    }

    const slot = this.slotPor(dia, horario);

    if (!slot) {
      return;
    }

    slot.disciplina = {
      id: disciplina.id,
      sigla: disciplina.sigla,
      descricao: disciplina.descricao
    };
  }

  remover(dia: DiaSemana, horario: string): void {
    const slot = this.slotPor(dia, horario);

    if (!slot) {
      return;
    }

    slot.disciplina = null;
  }

  salvarAlteracoes(): void {
    this.estadoSalvo = this.clonarEstado(this.estadoEditavel);
    this.matrizVisualService.salvar(this.estadoSalvo);
    this.successMessage = 'Alterações salvas com sucesso.';

    setTimeout(() => {
      this.successMessage = '';
    }, 2500);
  }

  desfazerAlteracoes(): void {
    this.estadoEditavel = this.clonarEstado(this.estadoSalvo);
    this.successMessage = '';
  }

  limparGrade(): void {
    const vazio = this.matrizVisualService.criarEstadoInicial();
    this.estadoSalvo = this.clonarEstado(vazio);
    this.estadoEditavel = this.clonarEstado(vazio);
    this.matrizVisualService.salvar(vazio);
    this.successMessage = 'Grade limpa com sucesso.';

    setTimeout(() => {
      this.successMessage = '';
    }, 2500);
  }

  private clonarEstado(state: MatrizVisualState): MatrizVisualState {
    return {
      slots: state.slots.map((slot) => ({
        dia: slot.dia,
        horario: slot.horario,
        disciplina: slot.disciplina
          ? {
              id: slot.disciplina.id,
              sigla: slot.disciplina.sigla,
              descricao: slot.disciplina.descricao
            }
          : null
      }))
    };
  }
}