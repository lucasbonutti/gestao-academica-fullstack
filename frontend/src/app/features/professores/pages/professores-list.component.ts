import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { forkJoin, of } from 'rxjs';
import { catchError } from 'rxjs/operators';

import { ProfessoresService } from '../services/professores.service';
import { EscolasService } from '../services/escolas.service';
import { FormacoesService } from '../services/formacoes.service';

import { ProfessorResponse } from '../../../core/models/professor.model';
import { EscolaResponse } from '../../../core/models/escola.model';
import { FormacaoResponse } from '../../../core/models/formacao.model';

import { AuthService } from '../../auth/services/auth.service';
import { AppShellComponent } from '../../../shared/components/app-shell.component';

type FiltroProfessor = 'todos' | 'ativos' | 'inativos';

@Component({
  selector: 'app-professores-list',
  standalone: true,
  imports: [CommonModule, RouterLink, RouterLinkActive, AppShellComponent],
  templateUrl: './professores-list.component.html',
  styleUrl: './professores-list.component.css'
})
export class ProfessoresListComponent implements OnInit {
  professores: ProfessorResponse[] = [];
  escolas: EscolaResponse[] = [];

  formacoesPorProfessor: Record<number, string> = {};

  loading = false;
  errorMessage = '';
  successMessage = '';

  page = 0;
  totalPages = 0;

  filtro: FiltroProfessor = 'todos';
  buscaId = '';
  escolaSelecionada = '';

  constructor(
    private professoresService: ProfessoresService,
    private escolasService: EscolasService,
    private formacoesService: FormacoesService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.carregarEscolas();
    this.carregarProfessores();
  }

  carregarEscolas(): void {
    this.escolasService.listarAtivas(0, 100).subscribe({
      next: (response) => {
        this.escolas = response.content;
      }
    });
  }

  carregarProfessores(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';
    this.formacoesPorProfessor = {};

    const escolaId = Number(this.escolaSelecionada);

    const request =
      this.escolaSelecionada && !isNaN(escolaId)
        ? this.professoresService.listarPorEscola(escolaId, this.page, 10)
        : this.filtro === 'ativos'
        ? this.professoresService.listarAtivos(this.page, 10)
        : this.filtro === 'inativos'
        ? this.professoresService.listarInativos(this.page, 10)
        : this.professoresService.listar(this.page, 10);

    request.subscribe({
      next: (response) => {
        this.professores = response.content;
        this.totalPages = response.totalPages;
        this.loading = false;
        this.carregarFormacoesDosProfessores();
      },
      error: (err: any) => {
        this.professores = [];
        this.totalPages = 0;
        this.errorMessage = err?.error?.message || 'Erro ao carregar professores.';
        this.loading = false;
      }
    });
  }

  carregarFormacoesDosProfessores(): void {
    this.formacoesPorProfessor = {};

    if (this.professores.length === 0) {
      return;
    }

    const requests = this.professores.map((professor) =>
      this.formacoesService.listarPorProfessor(professor.id, 0, 1).pipe(
        catchError(() => of({ content: [] }))
      )
    );

    forkJoin(requests).subscribe({
      next: (responses) => {
        responses.forEach((response: any, index) => {
          const professor = this.professores[index];
          const formacao = response.content?.[0];

          this.formacoesPorProfessor[professor.id] = formacao
            ? this.formatarFormacao(formacao)
            : 'Não informada';
        });
      }
    });
  }

  formatarFormacao(formacao: FormacaoResponse): string {
    return `${this.formatarTitulacao(formacao.titulacao)} em ${formacao.nomeCurso}`;
  }

  formatarTitulacao(titulacao: string): string {
    const nomes: Record<string, string> = {
      GRADUACAO: 'Graduação',
      ESPECIALIZACAO: 'Especialização',
      MBA: 'MBA',
      MESTRADO: 'Mestrado',
      DOUTORADO: 'Doutorado',
      POS_DOUTORADO: 'Pós-doutorado'
    };

    return nomes[titulacao] || titulacao;
  }

  buscarPorId(): void {
    this.errorMessage = '';
    this.successMessage = '';
    this.formacoesPorProfessor = {};

    if (!this.buscaId.trim()) {
      this.page = 0;
      this.carregarProfessores();
      return;
    }

    const id = Number(this.buscaId);

    if (isNaN(id)) {
      this.errorMessage = 'Informe um ID válido.';
      return;
    }

    this.loading = true;

    this.professoresService.buscarPorId(id).subscribe({
      next: (professor) => {
        this.professores = [professor];
        this.totalPages = 1;
        this.page = 0;
        this.loading = false;
        this.carregarFormacoesDosProfessores();
      },
      error: (err: any) => {
        this.professores = [];
        this.totalPages = 0;
        this.errorMessage = err?.error?.message || 'Professor não encontrado.';
        this.loading = false;
      }
    });
  }

  limparBusca(): void {
    this.buscaId = '';
    this.page = 0;
    this.carregarProfessores();
  }

  alterarFiltro(filtro: FiltroProfessor): void {
    this.filtro = filtro;
    this.escolaSelecionada = '';
    this.page = 0;
    this.carregarProfessores();
  }

  aplicarFiltroEscola(event: Event): void {
    this.escolaSelecionada = (event.target as HTMLSelectElement).value;
    this.page = 0;
    this.carregarProfessores();
  }

  proximaPagina(): void {
    if (this.page + 1 < this.totalPages) {
      this.page++;
      this.carregarProfessores();
    }
  }

  paginaAnterior(): void {
    if (this.page > 0) {
      this.page--;
      this.carregarProfessores();
    }
  }

  inativar(id: number): void {
    this.errorMessage = '';
    this.successMessage = '';

    this.professoresService.inativar(id).subscribe({
      next: () => {
        this.successMessage = 'Professor inativado com sucesso.';
        this.carregarProfessores();
      },
      error: (err: any) => {
        this.errorMessage = err?.error?.message || 'Erro ao inativar professor.';
      }
    });
  }

  ativar(id: number): void {
    this.errorMessage = '';
    this.successMessage = '';

    this.professoresService.ativar(id).subscribe({
      next: () => {
        this.successMessage = 'Professor ativado com sucesso.';
        this.carregarProfessores();
      },
      error: (err: any) => {
        this.errorMessage = err?.error?.message || 'Erro ao ativar professor.';
      }
    });
  }

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}