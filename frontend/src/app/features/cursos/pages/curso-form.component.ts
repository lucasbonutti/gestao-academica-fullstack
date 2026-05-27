import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { Router, RouterLink } from '@angular/router';
import { CursosService } from '../services/cursos.service';
import { EscolasService } from '../../professores/services/escolas.service';
import { EscolaResponse } from '../../../core/models/escola.model';
import { TurnoCurso } from '../../../core/models/curso.model';
import { AuthService } from '../../auth/services/auth.service';
import { AppShellComponent } from '../../../shared/components/app-shell.component';

@Component({
  selector: 'app-curso-form',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, AppShellComponent],
  templateUrl: './curso-form.component.html',
  styleUrl: './curso-form.component.css'
})
export class CursoFormComponent implements OnInit {
  sigla = '';
  descricao = '';
  escolaId = '';
  turno: TurnoCurso = 'MATUTINO';
  coordenador = '';

  escolas: EscolaResponse[] = [];

  loading = false;
  errorMessage = '';
  successMessage = '';

  turnos: TurnoCurso[] = ['MATUTINO', 'VESPERTINO', 'NOTURNO', 'INTEGRAL'];

  constructor(
    private cursosService: CursosService,
    private escolasService: EscolasService,
    private authService: AuthService,
    private router: Router
  ) {}

  ngOnInit(): void {
    this.carregarEscolas();
  }

  carregarEscolas(): void {
    this.escolasService.listarAtivas(0, 100).subscribe({
      next: (response) => {
        this.escolas = response.content;
      }
    });
  }

  salvar(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.cursosService.criar({
      sigla: this.sigla,
      descricao: this.descricao,
      escolaId: Number(this.escolaId),
      turno: this.turno,
      coordenador: this.coordenador
    }).subscribe({
      next: () => {
        this.successMessage = 'Curso cadastrado com sucesso!';
        this.loading = false;
        this.sigla = '';
        this.descricao = '';
        this.escolaId = '';
        this.turno = 'MATUTINO';
        this.coordenador = '';
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao cadastrar curso.';
        this.loading = false;
      }
    });
  }

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}