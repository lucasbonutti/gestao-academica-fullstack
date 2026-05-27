import { Component, Input, OnChanges, SimpleChanges } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { FormacoesService } from '../../features/professores/services/formacoes.service';
import { FormacaoResponse, Titulacao } from '../../core/models/formacao.model';

@Component({
  selector: 'app-professor-formacoes-panel',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './professor-formacoes-panel.component.html',
  styleUrl: './professor-formacoes-panel.component.css'
})
export class ProfessorFormacoesPanelComponent implements OnChanges {
  @Input() professorId!: number;

  formacoes: FormacaoResponse[] = [];
  loading = false;
  errorMessage = '';
  successMessage = '';

  titulacao: Titulacao = 'GRADUACAO';
  instituicao = '';
  nomeCurso = '';
  anoConclusao = new Date().getFullYear();

  titulacoes: Titulacao[] = [
    'GRADUACAO',
    'ESPECIALIZACAO',
    'MBA',
    'MESTRADO',
    'DOUTORADO',
    'POS_DOUTORADO'
  ];

  constructor(private formacoesService: FormacoesService) {}

  ngOnChanges(changes: SimpleChanges): void {
    if (changes['professorId']?.currentValue) {
      this.carregarFormacoes();
    }
  }

  carregarFormacoes(): void {
    if (!this.professorId) return;

    this.loading = true;
    this.errorMessage = '';

    this.formacoesService.listarPorProfessor(this.professorId).subscribe({
      next: (response) => {
        this.formacoes = response.content;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao carregar formações.';
        this.loading = false;
      }
    });
  }

  salvarFormacao(): void {
    this.errorMessage = '';
    this.successMessage = '';

    this.formacoesService.criar({
      professorId: this.professorId,
      titulacao: this.titulacao,
      instituicao: this.instituicao,
      nomeCurso: this.nomeCurso,
      anoConclusao: Number(this.anoConclusao)
    }).subscribe({
      next: () => {
        this.successMessage = 'Formação cadastrada com sucesso.';
        this.instituicao = '';
        this.nomeCurso = '';
        this.anoConclusao = new Date().getFullYear();
        this.titulacao = 'GRADUACAO';
        this.carregarFormacoes();
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao cadastrar formação.';
      }
    });
  }

  excluirFormacao(id: number): void {
    this.formacoesService.excluir(id).subscribe({
      next: () => {
        this.successMessage = 'Formação removida com sucesso.';
        this.carregarFormacoes();
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao remover formação.';
      }
    });
  }

  formatarTitulo(titulo: string): string {
    return titulo.replaceAll('_', ' ');
  }
}