import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { Router, RouterLink } from '@angular/router';
import { AppShellComponent } from '../../../shared/components/app-shell.component';
import { AuthService } from '../../auth/services/auth.service';
import { MatrizesCurricularesService } from '../services/matrizes-curriculares.service';
import { CursosService } from '../../cursos/services/cursos.service';
import { MatrizCurricularResponse } from '../../../core/models/matriz-curricular.model';
import { CursoResponse } from '../../../core/models/curso.model';

type FiltroMatriz = 'todas' | 'ativas' | 'inativas';

@Component({
  selector: 'app-matrizes-list',
  standalone: true,
  imports: [CommonModule, RouterLink, AppShellComponent],
  templateUrl: './matrizes-list.component.html',
  styleUrl: './matrizes-list.component.css'
})
export class MatrizesListComponent implements OnInit {
  matrizes: MatrizCurricularResponse[] = [];
  cursos: CursoResponse[] = [];

  loading = false;
  errorMessage = '';
  successMessage = '';

  page = 0;
  totalPages = 0;
  filtro: FiltroMatriz = 'todas';
  cursoSelecionado = '';

  constructor(
    private matrizesService: MatrizesCurricularesService,
    private cursosService: CursosService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.carregarCursos();
    this.carregarMatrizes();
  }

  carregarCursos(): void {
    this.cursosService.listarAtivos(0, 100).subscribe({
      next: (response) => {
        this.cursos = response.content;
      },
      error: (err: any) => {
        this.errorMessage = err?.error?.message || 'Erro ao carregar cursos.';
      }
    });
  }

  carregarMatrizes(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    const cursoId = Number(this.cursoSelecionado);

    const request =
      this.cursoSelecionado && !isNaN(cursoId)
        ? this.matrizesService.listarPorCurso(cursoId, this.page, 10)
        : this.filtro === 'ativas'
        ? this.matrizesService.listarAtivas(this.page, 10)
        : this.filtro === 'inativas'
        ? this.matrizesService.listarInativas(this.page, 10)
        : this.matrizesService.listar(this.page, 10);

    request.subscribe({
      next: (response) => {
        this.matrizes = response.content;
        this.totalPages = response.totalPages;
        this.loading = false;
      },
      error: (err: any) => {
        this.matrizes = [];
        this.totalPages = 0;
        this.errorMessage = err?.error?.message || 'Erro ao carregar matrizes.';
        this.loading = false;
      }
    });
  }

  alterarFiltro(filtro: FiltroMatriz): void {
    this.filtro = filtro;
    this.cursoSelecionado = '';
    this.page = 0;
    this.errorMessage = '';
    this.successMessage = '';
    this.carregarMatrizes();
  }

  aplicarFiltroCurso(event: Event): void {
    this.cursoSelecionado = (event.target as HTMLSelectElement).value;
    this.page = 0;
    this.errorMessage = '';
    this.successMessage = '';
    this.carregarMatrizes();
  }

  ativar(id: number): void {
    this.errorMessage = '';
    this.successMessage = '';

    this.matrizesService.ativar(id).subscribe({
      next: () => {
        this.successMessage = 'Matriz ativada com sucesso.';
        this.carregarMatrizes();
      },
      error: (err: any) => {
        this.errorMessage = err?.error?.message || 'Erro ao ativar matriz.';
      }
    });
  }

  inativar(id: number): void {
    this.errorMessage = '';
    this.successMessage = '';

    this.matrizesService.inativar(id).subscribe({
      next: () => {
        this.successMessage = 'Matriz inativada com sucesso.';
        this.carregarMatrizes();
      },
      error: (err: any) => {
        this.errorMessage = err?.error?.message || 'Erro ao inativar matriz.';
      }
    });
  }

  paginaAnterior(): void {
    if (this.page > 0) {
      this.page--;
      this.carregarMatrizes();
    }
  }

  proximaPagina(): void {
    if (this.page + 1 < this.totalPages) {
      this.page++;
      this.carregarMatrizes();
    }
  }

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}