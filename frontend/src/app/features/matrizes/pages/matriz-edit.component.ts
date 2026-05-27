import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AppShellComponent } from '../../../shared/components/app-shell.component';
import { AuthService } from '../../auth/services/auth.service';
import { MatrizesCurricularesService } from '../services/matrizes-curriculares.service';
import { CursosService } from '../../cursos/services/cursos.service';
import { CursoResponse } from '../../../core/models/curso.model';

@Component({
  selector: 'app-matriz-edit',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, AppShellComponent],
  templateUrl: './matriz-edit.component.html',
  styleUrl: './matriz-edit.component.css'
})
export class MatrizEditComponent implements OnInit {
  id!: number;
  nome = '';
  descricao = '';
  cursoId = '';

  cursos: CursoResponse[] = [];

  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private matrizesService: MatrizesCurricularesService,
    private cursosService: CursosService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (!idParam) {
      this.errorMessage = 'ID da matriz não informado.';
      return;
    }

    this.id = Number(idParam);

    if (isNaN(this.id)) {
      this.errorMessage = 'ID inválido.';
      return;
    }

    this.carregarCursos();
    this.carregarMatriz();
  }

  carregarCursos(): void {
    this.cursosService.listarAtivos(0, 100).subscribe({
      next: (response) => {
        this.cursos = response.content;
      }
    });
  }

  carregarMatriz(): void {
    this.loading = true;
    this.errorMessage = '';

    this.matrizesService.buscarPorId(this.id).subscribe({
      next: (matriz) => {
        this.nome = matriz.nome;
        this.descricao = matriz.descricao;
        this.cursoId = String(matriz.curso.id);
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao carregar matriz.';
        this.loading = false;
      }
    });
  }

  salvar(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.matrizesService.atualizar({
      id: this.id,
      nome: this.nome,
      descricao: this.descricao,
      cursoId: Number(this.cursoId)
    }).subscribe({
      next: () => {
        this.successMessage = 'Matriz atualizada com sucesso.';
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao atualizar matriz.';
        this.loading = false;
      }
    });
  }

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}