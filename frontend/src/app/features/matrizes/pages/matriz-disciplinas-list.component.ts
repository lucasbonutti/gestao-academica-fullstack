import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AppShellComponent } from '../../../shared/components/app-shell.component';
import { AuthService } from '../../auth/services/auth.service';
import { MatrizDisciplinasService } from '../services/matriz-disciplinas.service';
import { MatrizesCurricularesService } from '../services/matrizes-curriculares.service';
import { MatrizDisciplinaResponse } from '../../../core/models/matriz-disciplina.model';
import { MatrizCurricularResponse } from '../../../core/models/matriz-curricular.model';

@Component({
  selector: 'app-matriz-disciplinas-list',
  standalone: true,
  imports: [CommonModule, RouterLink, AppShellComponent],
  templateUrl: './matriz-disciplinas-list.component.html',
  styleUrl: './matriz-disciplinas-list.component.css'
})
export class MatrizDisciplinasListComponent implements OnInit {
  matrizId!: number;
  matriz?: MatrizCurricularResponse;
  itens: MatrizDisciplinaResponse[] = [];

  loading = false;
  errorMessage = '';
  successMessage = '';

  page = 0;
  totalPages = 0;

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private authService: AuthService,
    private matrizesService: MatrizesCurricularesService,
    private matrizDisciplinasService: MatrizDisciplinasService
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (!idParam) {
      this.errorMessage = 'ID da matriz não informado.';
      return;
    }

    this.matrizId = Number(idParam);

    if (isNaN(this.matrizId)) {
      this.errorMessage = 'ID da matriz inválido.';
      return;
    }

    this.carregarMatriz();
    this.carregarDisciplinasDaMatriz();
  }

  carregarMatriz(): void {
    this.matrizesService.buscarPorId(this.matrizId).subscribe({
      next: (response) => {
        this.matriz = response;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao carregar matriz curricular.';
      }
    });
  }

  carregarDisciplinasDaMatriz(): void {
    this.loading = true;
    this.errorMessage = '';

    this.matrizDisciplinasService.listarPorMatriz(this.matrizId, this.page, 20).subscribe({
      next: (response) => {
        this.itens = response.content;
        this.totalPages = response.totalPages;
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao carregar disciplinas da matriz.';
        this.loading = false;
      }
    });
  }

  paginaAnterior(): void {
    if (this.page > 0) {
      this.page--;
      this.carregarDisciplinasDaMatriz();
    }
  }

  proximaPagina(): void {
    if (this.page + 1 < this.totalPages) {
      this.page++;
      this.carregarDisciplinasDaMatriz();
    }
  }

  formatarPreRequisitos(item: MatrizDisciplinaResponse): string {
    if (!item.preRequisitos || item.preRequisitos.length === 0) {
      return 'Sem pré-requisitos';
    }

    return item.preRequisitos.map((disciplina) => disciplina.sigla).join(', ');
  }

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}