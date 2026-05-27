import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';
import { ActivatedRoute, Router, RouterLink } from '@angular/router';
import { AppShellComponent } from '../../../shared/components/app-shell.component';
import { AuthService } from '../../auth/services/auth.service';
import { DisciplinasService } from '../services/disciplinas.service';
import { EscolasService } from '../../escolas/services/escolas.service';
import { EscolaResponse } from '../../../core/models/escola.model';

@Component({
  selector: 'app-disciplina-edit',
  standalone: true,
  imports: [CommonModule, FormsModule, RouterLink, AppShellComponent],
  templateUrl: './disciplina-edit.component.html',
  styleUrl: './disciplina-edit.component.css'
})
export class DisciplinaEditComponent implements OnInit {
  id!: number;
  sigla = '';
  descricao = '';
  cargaHoraria = 60;
  escolaId = '';

  escolas: EscolaResponse[] = [];

  loading = false;
  errorMessage = '';
  successMessage = '';

  constructor(
    private route: ActivatedRoute,
    private router: Router,
    private disciplinasService: DisciplinasService,
    private escolasService: EscolasService,
    private authService: AuthService
  ) {}

  ngOnInit(): void {
    const idParam = this.route.snapshot.paramMap.get('id');

    if (!idParam) {
      this.errorMessage = 'ID da disciplina não informado.';
      return;
    }

    this.id = Number(idParam);

    if (isNaN(this.id)) {
      this.errorMessage = 'ID da disciplina inválido.';
      return;
    }

    this.carregarEscolas();
    this.carregarDisciplina();
  }

  carregarEscolas(): void {
    this.escolasService.listarAtivas(0, 100).subscribe({
      next: (response) => {
        this.escolas = response.content;
      }
    });
  }

  carregarDisciplina(): void {
    this.loading = true;
    this.errorMessage = '';

    this.disciplinasService.buscarPorId(this.id).subscribe({
      next: (disciplina) => {
        this.sigla = disciplina.sigla;
        this.descricao = disciplina.descricao;
        this.cargaHoraria = disciplina.cargaHoraria;
        this.escolaId = String(disciplina.escola.id);
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao carregar disciplina.';
        this.loading = false;
      }
    });
  }

  salvar(): void {
    this.loading = true;
    this.errorMessage = '';
    this.successMessage = '';

    this.disciplinasService.atualizar({
      id: this.id,
      sigla: this.sigla,
      descricao: this.descricao,
      cargaHoraria: Number(this.cargaHoraria),
      escolaId: Number(this.escolaId)
    }).subscribe({
      next: () => {
        this.successMessage = 'Disciplina atualizada com sucesso.';
        this.loading = false;
      },
      error: (err) => {
        this.errorMessage = err?.error?.message || 'Erro ao atualizar disciplina.';
        this.loading = false;
      }
    });
  }

  sair(): void {
    this.authService.logout();
    this.router.navigate(['/login']);
  }
}