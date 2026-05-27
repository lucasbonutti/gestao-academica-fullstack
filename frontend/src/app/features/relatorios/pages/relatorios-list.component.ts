import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { FormsModule } from '@angular/forms';

import { AppShellComponent } from '../../../shared/components/app-shell.component';
import { AuthService } from '../../auth/services/auth.service';
import { RelatoriosMonitoriaService } from '../services/relatorios-monitoria.service';
import { RelatorioMonitoriaResponse } from '../../../core/models/relatorio-monitoria.model';

type FiltroRelatorio =
  | 'todos'
  | 'professor'
  | 'aluno'
  | 'disciplina'
  | 'semestre'
  | 'status';

@Component({
  selector: 'app-relatorios-list',
  standalone: true,
  imports: [
    CommonModule,
    FormsModule, // ✅ NECESSÁRIO pro ngModel
    RouterLink,
    AppShellComponent
  ],
  templateUrl: './relatorios-list.component.html',
  styleUrl: './relatorios-list.component.css'
})
export class RelatoriosListComponent implements OnInit {
  relatorios: RelatorioMonitoriaResponse[] = [];

  loading = false;
  errorMessage = '';
  successMessage = '';

  page = 0;
  totalPages = 0;

  filtro: FiltroRelatorio = 'todos';

  professorIdBusca = '';
  alunoIdBusca = '';
  disciplinaIdBusca = '';
  semestreBusca = '';
  statusBusca = '';

  // ✅ CONTROLE DO FILTRO EXPANSÍVEL
  filtrosAbertos = false;

  constructor(
    private relatoriosService: RelatoriosMonitoriaService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.carregarRelatorios();
  }

  // ✅ ABRIR / FECHAR FILTROS
  toggleFiltros(): void {
    this.filtrosAbertos = !this.filtrosAbertos;
  }

  carregarRelatorios(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const professorId = Number(this.professorIdBusca);
    const alunoId = Number(this.alunoIdBusca);
    const disciplinaId = Number(this.disciplinaIdBusca);

    const request =
      this.filtro === 'professor' && this.professorIdBusca && !isNaN(professorId)
        ? this.relatoriosService.listarPorProfessor(professorId, this.page, 10)
        : this.filtro === 'aluno' && this.alunoIdBusca && !isNaN(alunoId)
        ? this.relatoriosService.listarPorAluno(alunoId, this.page, 10)
        : this.filtro === 'disciplina' && this.disciplinaIdBusca && !isNaN(disciplinaId)
        ? this.relatoriosService.listarPorDisciplina(disciplinaId, this.page, 10)
        : this.filtro === 'semestre' && this.semestreBusca
        ? this.relatoriosService.listarPorSemestre(this.semestreBusca, this.page, 10)
        : this.filtro === 'status' && this.statusBusca
        ? this.relatoriosService.listarPorStatus(this.statusBusca, this.page, 10)
        : this.relatoriosService.listar(this.page, 10);

    request.subscribe({
      next: (response) => {
        this.relatorios = response.content;
        this.totalPages = response.totalPages;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage =
          err?.error?.message || 'Erro ao carregar relatórios.';
        this.loading = false;
      }
    });
  }

  aplicarFiltro(tipo: FiltroRelatorio): void {
    this.filtro = tipo;
    this.page = 0;
    this.carregarRelatorios();
  }

  limparFiltros(): void {
    this.filtro = 'todos';
    this.professorIdBusca = '';
    this.alunoIdBusca = '';
    this.disciplinaIdBusca = '';
    this.semestreBusca = '';
    this.statusBusca = '';
    this.page = 0;
    this.carregarRelatorios();
  }

  paginaAnterior(): void {
    if (this.page > 0) {
      this.page--;
      this.carregarRelatorios();
    }
  }

  proximaPagina(): void {
    if (this.page + 1 < this.totalPages) {
      this.page++;
      this.carregarRelatorios();
    }
  }

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}