import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { AppShellComponent } from '../../../shared/components/app-shell.component';
import { AuthService } from '../../auth/services/auth.service';
import { MatrizesCurricularesService } from '../services/matrizes-curriculares.service';
import { CursosService } from '../../cursos/services/cursos.service';
import { CursoResponse } from '../../../core/models/curso.model';

@Component({
  selector: 'app-matriz-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, AppShellComponent],
  templateUrl: './matriz-form.component.html',
  styleUrl: './matriz-form.component.css'
})
export class MatrizFormComponent implements OnInit {
  nome = '';
  descricao = '';
  cursoId = '';

  cursos: CursoResponse[] = [];

  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private matrizesService: MatrizesCurricularesService,
    private cursosService: CursosService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.cursosService.listarAtivos(0, 100).subscribe({
      next: (response) => {
        this.cursos = response.content;
      }
    });
  }

  salvar(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.matrizesService.criar({
      nome: this.nome,
      descricao: this.descricao,
      cursoId: Number(this.cursoId)
    }).subscribe({
      next: () => {
        this.successMessage = 'Matriz curricular cadastrada com sucesso.';
        this.loading = false;
        this.nome = '';
        this.descricao = '';
        this.cursoId = '';
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao cadastrar matriz.';
        this.loading = false;
      }
    });
  }

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}