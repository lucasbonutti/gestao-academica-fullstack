import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink, RouterLinkActive } from '@angular/router';
import { AlunosService } from '../services/alunos.service';
import { AlunoResponse } from '../../../core/models/aluno.model';
import { AuthService } from '../../auth/services/auth.service';
import { AppShellComponent } from '../../../shared/components/app-shell.component';

type FiltroAluno = 'todos' | 'ativos' | 'inativos';

@Component({
  selector: 'app-alunos-list',
  standalone: true,
  imports: [CommonModule, RouterLink, AppShellComponent],
  templateUrl: './alunos-list.component.html',
  styleUrl: './alunos-list.component.css'
})

export class AlunosListComponent implements OnInit {
  alunos: AlunoResponse[] = [];
  loading = false;
  errorMessage = '';
  successMessage = '';

  page = 0;
  totalPages = 0;

  filtro: FiltroAluno = 'todos';
  buscaId = '';

  constructor(
    private alunosService: AlunosService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.carregarAlunos();
  }

  carregarAlunos(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const request =
      this.filtro === 'ativos'
        ? this.alunosService.listarAtivos(this.page, 10)
        : this.filtro === 'inativos'
        ? this.alunosService.listarInativos(this.page, 10)
        : this.alunosService.listar(this.page, 10);

    request.subscribe({
      next: (response) => {
        this.alunos = response.content;
        this.totalPages = response.totalPages;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao carregar alunos.';
        this.loading = false;
      }
    });
  }

  buscarPorId(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.buscaId.trim()) {
      this.page = 0;
      this.carregarAlunos();
      return;
    }

    const id = Number(this.buscaId);

    if (isNaN(id)) {
      this.errorMessage = 'Informe um ID válido.';
      return;
    }

    this.loading = true;
    this.alunosService.buscarPorId(id).subscribe({
      next: (aluno) => {
        this.alunos = [aluno];
        this.totalPages = 1;
        this.page = 0;
        this.loading = false;
      },
      error: (err) => {
        this.alunos = [];
        this.errorMessage = err?.error?.message || 'Aluno não encontrado.';
        this.loading = false;
      }
    });
  }

  limparBusca(): void {
    this.buscaId = '';
    this.page = 0;
    this.carregarAlunos();
  }

  alterarFiltro(filtro: FiltroAluno): void {
    this.filtro = filtro;
    this.page = 0;
    this.carregarAlunos();
  }

  proximaPagina(): void {
    if (this.page + 1 < this.totalPages) {
      this.page++;
      this.carregarAlunos();
    }
  }

  paginaAnterior(): void {
    if (this.page > 0) {
      this.page--;
      this.carregarAlunos();
    }
  }

  inativar(id: number): void {
    this.errorMessage = '';
    this.successMessage = '';

    this.alunosService.inativar(id).subscribe({
      next: () => {
        this.successMessage = 'Aluno inativado com sucesso.';
        this.carregarAlunos();
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao inativar aluno.';
      }
    });
  }

  ativar(id: number): void {
    this.errorMessage = '';
    this.successMessage = '';

    this.alunosService.ativar(id).subscribe({
      next: () => {
        this.successMessage = 'Aluno ativado com sucesso.';
        this.carregarAlunos();
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao ativar aluno.';
      }
    });
  }

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}