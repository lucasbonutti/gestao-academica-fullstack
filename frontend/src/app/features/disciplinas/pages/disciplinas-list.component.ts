import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AppShellComponent } from '../../../shared/components/app-shell.component';
import { AuthService } from '../../auth/services/auth.service';
import { DisciplinasService } from '../services/disciplinas.service';
import { EscolasService } from '../../escolas/services/escolas.service';
import { DisciplinaMatrizCard, DisciplinaResponse } from '../../../core/models/disciplina.model';
import { EscolaResponse } from '../../../core/models/escola.model';
import { MatrizPlannerComponent } from '../../../shared/components/matriz-planner.component';

type FiltroDisciplina = 'todas' | 'ativas' | 'inativas';

@Component({
  selector: 'app-disciplinas-list',
  standalone: true,
  imports: [CommonModule, RouterLink, AppShellComponent, MatrizPlannerComponent],
  templateUrl: './disciplinas-list.component.html',
  styleUrl: './disciplinas-list.component.css'
})
export class DisciplinasListComponent implements OnInit {
  disciplinas: DisciplinaResponse[] = [];
  escolas: EscolaResponse[] = [];
  disciplinasMatriz: DisciplinaMatrizCard[] = [];

  loading = false;
  errorMessage = '';
  successMessage = '';

  page = 0;
  totalPages = 0;
  filtro: FiltroDisciplina = 'todas';
  escolaSelecionada = '';

  plannerAberto = false;

  constructor(
    private disciplinasService: DisciplinasService,
    private escolasService: EscolasService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.carregarEscolas();
    this.carregarDisciplinas();
  }

  carregarEscolas(): void {
    this.escolasService.listarAtivas(0, 100).subscribe({
      next: (response) => {
        this.escolas = response.content;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao carregar escolas.';
      }
    });
  }

  carregarDisciplinas(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const escolaId = Number(this.escolaSelecionada);

    const request =
      this.escolaSelecionada && !isNaN(escolaId)
        ? this.disciplinasService.listarPorEscola(escolaId, this.page, 10)
        : this.filtro === 'ativas'
        ? this.disciplinasService.listarAtivas(this.page, 10)
        : this.filtro === 'inativas'
        ? this.disciplinasService.listarInativas(this.page, 10)
        : this.disciplinasService.listar(this.page, 10);

    request.subscribe({
      next: (response) => {
        this.errorMessage = '';
        this.disciplinas = response.content;
        this.totalPages = response.totalPages;
        this.disciplinasMatriz = response.content.map((item) => ({
          id: item.id,
          sigla: item.sigla,
          descricao: item.descricao
        }));

        /* deixa fechado por padrão sempre que recarregar */
        this.plannerAberto = false;

        this.loading = false;
      },
      error: (err) => {
        this.disciplinas = [];
        this.disciplinasMatriz = [];
        this.totalPages = 0;
        this.plannerAberto = false;
        this.errorMessage = err?.error?.message || 'Erro ao carregar disciplinas.';
        this.loading = false;
      }
    });
  }

  alterarFiltro(filtro: FiltroDisciplina): void {
    this.filtro = filtro;
    this.escolaSelecionada = '';
    this.page = 0;
    this.errorMessage = '';
    this.successMessage = '';
    this.carregarDisciplinas();
  }

  aplicarFiltroEscola(event: Event): void {
    this.escolaSelecionada = (event.target as HTMLSelectElement).value;
    this.page = 0;
    this.errorMessage = '';
    this.successMessage = '';
    this.carregarDisciplinas();
  }

  ativar(id: number): void {
    this.errorMessage = '';
    this.successMessage = '';

    this.disciplinasService.ativar(id).subscribe({
      next: () => {
        this.errorMessage = '';
        this.successMessage = 'Disciplina ativada com sucesso.';
        this.carregarDisciplinas();
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao ativar disciplina.';
      }
    });
  }

  inativar(id: number): void {
    this.errorMessage = '';
    this.successMessage = '';

    this.disciplinasService.inativar(id).subscribe({
      next: () => {
        this.errorMessage = '';
        this.successMessage = 'Disciplina inativada com sucesso.';
        this.carregarDisciplinas();
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao inativar disciplina.';
      }
    });
  }

  paginaAnterior(): void {
    if (this.page > 0) {
      this.page--;
      this.errorMessage = '';
      this.carregarDisciplinas();
    }
  }

  proximaPagina(): void {
    if (this.page + 1 < this.totalPages) {
      this.page++;
      this.errorMessage = '';
      this.carregarDisciplinas();
    }
  }

  togglePlanner(): void {
    this.plannerAberto = !this.plannerAberto;
  }

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}