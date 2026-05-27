import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { CursosService } from '../services/cursos.service';
import { EscolasService } from '../../professores/services/escolas.service';
import { EscolaResponse } from '../../../core/models/escola.model';
import { TurnoCurso } from '../../../core/models/curso.model';
import { AuthService } from '../../auth/services/auth.service';
import { AppShellComponent } from '../../../shared/components/app-shell.component';

@Component({
  selector: 'app-curso-edit',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, AppShellComponent],
  templateUrl: './curso-edit.component.html',
  styleUrl: './curso-edit.component.css'
})
export class CursoEditComponent implements OnInit {
  id!: number;
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
    private route: ActivatedRoute,
    private router: Router,
    private cursosService: CursosService,
    private escolasService: EscolasService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (!idParam) {
      this.errorMessage = 'ID do curso não informado.';
      return;
    }

    this.id = Number(idParam);

    if (isNaN(this.id)) {
      this.errorMessage = 'ID inválido.';
      return;
    }

    this.carregarEscolas();
    this.carregarCurso();
  }

  carregarEscolas(): void {
    this.escolasService.listarAtivas(0, 100).subscribe({
      next: (response) => {
        this.escolas = response.content;
      }
    });
  }

  carregarCurso(): void {
    this.loading = true;
    this.cursosService.buscarPorId(this.id).subscribe({
      next: (curso) => {
        this.sigla = curso.sigla;
        this.descricao = curso.descricao;
        this.turno = curso.turno;
        this.coordenador = curso.coordenador;
        this.escolaId = String(curso.escola.id);
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao carregar curso.';
        this.loading = false;
      }
    });
  }

  salvar(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.cursosService.atualizar({
      id: this.id,
      sigla: this.sigla,
      descricao: this.descricao,
      escolaId: Number(this.escolaId),
      turno: this.turno,
      coordenador: this.coordenador
    }).subscribe({
      next: () => {
        this.successMessage = 'Curso atualizado com sucesso!';
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao atualizar curso.';
        this.loading = false;
      }
    });
  }

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}