import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { EscolasService } from '../services/escolas.service';
import { EscolaResponse } from '../../../core/models/escola.model';
import { AuthService } from '../../auth/services/auth.service';
import { AppShellComponent } from '../../../shared/components/app-shell.component';

type FiltroEscola = 'todos' | 'ativos' | 'inativos';

@Component({
  selector: 'app-escolas-list',
  standalone: true,
  imports: [CommonModule, RouterLink, AppShellComponent],
  templateUrl: './escolas-list.component.html',
  styleUrl: './escolas-list.component.css'
})
export class EscolasListComponent implements OnInit {
  escolas: EscolaResponse[] = [];
  loading = false;
  errorMessage = '';
  successMessage = '';

  page = 0;
  totalPages = 0;
  filtro: FiltroEscola = 'todos';
  buscaId = '';

  constructor(
    private escolasService: EscolasService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.carregarEscolas();
  }

  carregarEscolas(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const request =
      this.filtro === 'ativos'
        ? this.escolasService.listarAtivas(this.page, 10)
        : this.filtro === 'inativos'
        ? this.escolasService.listarInativas(this.page, 10)
        : this.escolasService.listar(this.page, 10);

    request.subscribe({
      next: (response) => {
        this.escolas = response.content;
        this.totalPages = response.totalPages;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao carregar escolas.';
        this.loading = false;
      }
    });
  }

  buscarPorId(): void {
    this.errorMessage = '';
    this.successMessage = '';

    if (!this.buscaId.trim()) {
      this.page = 0;
      this.carregarEscolas();
      return;
    }

    const id = Number(this.buscaId);

    if (isNaN(id)) {
      this.errorMessage = 'Informe um ID válido.';
      return;
    }

    this.loading = true;
    this.escolasService.buscarPorId(id).subscribe({
      next: (escola) => {
        this.escolas = [escola];
        this.totalPages = 1;
        this.page = 0;
        this.loading = false;
      },
      error: (err) => {
        this.escolas = [];
        this.errorMessage = err?.error?.message || 'Escola não encontrada.';
        this.loading = false;
      }
    });
  }

  limparBusca(): void {
    this.buscaId = '';
    this.page = 0;
    this.carregarEscolas();
  }

  alterarFiltro(filtro: FiltroEscola): void {
    this.filtro = filtro;
    this.page = 0;
    this.carregarEscolas();
  }

  proximaPagina(): void {
    if (this.page + 1 < this.totalPages) {
      this.page++;
      this.carregarEscolas();
    }
  }

  paginaAnterior(): void {
    if (this.page > 0) {
      this.page--;
      this.carregarEscolas();
    }
  }

  ativar(id: number): void {
    this.escolasService.ativar(id).subscribe({
      next: () => {
        this.successMessage = 'Escola ativada com sucesso.';
        this.carregarEscolas();
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao ativar escola.';
      }
    });
  }

  inativar(id: number): void {
    this.escolasService.inativar(id).subscribe({
      next: () => {
        this.successMessage = 'Escola inativada com sucesso.';
        this.carregarEscolas();
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao inativar escola.';
      }
    });
  }

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}