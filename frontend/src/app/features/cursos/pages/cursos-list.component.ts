import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { CursosService } from '../services/cursos.service';
import { EscolasService } from '../../professores/services/escolas.service';
import { CursoResponse } from '../../../core/models/curso.model';
import { EscolaResponse } from '../../../core/models/escola.model';
import { AuthService } from '../../auth/services/auth.service';
import { AppShellComponent } from '../../../shared/components/app-shell.component';

type FiltroCurso = 'todos' | 'ativos' | 'inativos';

@Component({
  selector: 'app-cursos-list',
  standalone: true,
  imports: [CommonModule, RouterLink, AppShellComponent],
  templateUrl: './cursos-list.component.html',
  styleUrl: './cursos-list.component.css'
})
export class CursosListComponent implements OnInit {
  cursos: CursoResponse[] = [];
  escolas: EscolaResponse[] = [];

  loading = false;
  errorMessage = '';
  successMessage = '';

  page = 0;
  totalPages = 0;
  filtro: FiltroCurso = 'todos';
  escolaSelecionada = '';

  constructor(
    private cursosService: CursosService,
    private escolasService: EscolasService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.carregarEscolas();
    this.carregarCursos();
  }

  carregarEscolas(): void {
    this.escolasService.listarAtivas(0, 100).subscribe({
      next: (response) => {
        this.escolas = response.content;
      }
    });
  }

  carregarCursos(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const escolaId = Number(this.escolaSelecionada);

    const request =
      this.escolaSelecionada && !isNaN(escolaId)
        ? this.cursosService.listarPorEscola(escolaId, this.page, 10)
        : this.filtro === 'ativos'
        ? this.cursosService.listarAtivos(this.page, 10)
        : this.filtro === 'inativos'
        ? this.cursosService.listarInativos(this.page, 10)
        : this.cursosService.listar(this.page, 10);

    request.subscribe({
      next: (response) => {
        this.cursos = response.content;
        this.totalPages = response.totalPages;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao carregar cursos.';
        this.loading = false;
      }
    });
  }

  alterarFiltro(filtro: FiltroCurso): void {
    this.filtro = filtro;
    this.escolaSelecionada = '';
    this.page = 0;
    this.carregarCursos();
  }

  aplicarFiltroEscola(event: Event): void {
    this.escolaSelecionada = (event.target as HTMLSelectElement).value;
    this.page = 0;
    this.carregarCursos();
  }

  proximaPagina(): void {
    if (this.page + 1 < this.totalPages) {
      this.page++;
      this.carregarCursos();
    }
  }

  paginaAnterior(): void {
    if (this.page > 0) {
      this.page--;
      this.carregarCursos();
    }
  }

  inativar(id: number): void {
    this.cursosService.inativar(id).subscribe({
      next: () => {
        this.successMessage = 'Curso inativado com sucesso.';
        this.carregarCursos();
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao inativar curso.';
      }
    });
  }

  ativar(id: number): void {
    this.cursosService.ativar(id).subscribe({
      next: () => {
        this.successMessage = 'Curso ativado com sucesso.';
        this.carregarCursos();
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao ativar curso.';
      }
    });
  }

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}