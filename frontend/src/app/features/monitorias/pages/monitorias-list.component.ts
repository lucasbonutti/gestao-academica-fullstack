import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AppShellComponent } from '../../../shared/components/app-shell.component';
import { AuthService } from '../../auth/services/auth.service';
import { MonitoriasService } from '../services/monitorias.service';
import { MonitoriaResponse, StatusMonitoria } from '../../../core/models/monitoria.model';

type FiltroMonitoria = 'todas' | 'EM_ANDAMENTO' | 'FINALIZADA' | 'CANCELADA';

@Component({
  selector: 'app-monitorias-list',
  standalone: true,
  imports: [CommonModule, RouterLink, AppShellComponent],
  templateUrl: './monitorias-list.component.html',
  styleUrl: './monitorias-list.component.css'
})
export class MonitoriasListComponent implements OnInit {
  monitorias: MonitoriaResponse[] = [];

  loading = false;
  errorMessage = '';
  successMessage = '';

  page = 0;
  totalPages = 0;

  filtro: FiltroMonitoria = 'todas';
  professorIdBusca = '';
  alunoIdBusca = '';

  filtrosAbertos = false;

  constructor(
    private monitoriasService: MonitoriasService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.carregarMonitorias();
  }

  carregarMonitorias(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const professorId = Number(this.professorIdBusca);
    const alunoId = Number(this.alunoIdBusca);

    const request =
      this.professorIdBusca && !isNaN(professorId)
        ? this.monitoriasService.listarPorProfessor(professorId, this.page, 10)
        : this.alunoIdBusca && !isNaN(alunoId)
        ? this.monitoriasService.listarPorAluno(alunoId, this.page, 10)
        : this.filtro !== 'todas'
        ? this.monitoriasService.listarPorStatus(this.filtro, this.page, 10)
        : this.monitoriasService.listar(this.page, 10);

    request.subscribe({
      next: (response) => {
        this.monitorias = response.content;
        this.totalPages = response.totalPages;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao carregar monitorias.';
        this.loading = false;
      }
    });
  }

  alterarFiltro(filtro: FiltroMonitoria): void {
    this.filtro = filtro;
    this.professorIdBusca = '';
    this.alunoIdBusca = '';
    this.page = 0;
    this.carregarMonitorias();
  }

  buscarPorProfessor(): void {
    this.alunoIdBusca = '';
    this.page = 0;
    this.carregarMonitorias();
  }

  buscarPorAluno(): void {
    this.professorIdBusca = '';
    this.page = 0;
    this.carregarMonitorias();
  }

  limparFiltros(): void {
    this.filtro = 'todas';
    this.professorIdBusca = '';
    this.alunoIdBusca = '';
    this.page = 0;
    this.carregarMonitorias();
  }

  paginaAnterior(): void {
    if (this.page > 0) {
      this.page--;
      this.carregarMonitorias();
    }
  }

  proximaPagina(): void {
    if (this.page + 1 < this.totalPages) {
      this.page++;
      this.carregarMonitorias();
    }
  }

  statusClass(status: StatusMonitoria): string {
    if (status === 'EM_ANDAMENTO') return 'status-active';
    if (status === 'FINALIZADA') return 'status-finished';
    return 'status-inactive';
  }

  toggleFiltros(): void {
    this.filtrosAbertos = !this.filtrosAbertos;
  }

finalizar(id: number): void {
  this.errorMessage = '';
  this.successMessage = '';

  this.monitoriasService.finalizar(id).subscribe({
    next: () => {
      this.successMessage = 'Monitoria finalizada com sucesso.';
      this.carregarMonitorias();
    },
    error: (err: any) => {
      this.errorMessage = err?.error?.message || 'Erro ao finalizar monitoria.';
    }
  });
}

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}